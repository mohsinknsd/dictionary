package com.square.dictionary.controller;

import static com.square.dictionary.model.Constants.MESSAGE;
import static com.square.dictionary.model.Constants.STATUS;

import java.io.StringWriter;
import java.util.Date;
import java.util.UUID;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.square.dictionary.config.MailConfiguration;
import com.square.dictionary.model.User;
import com.square.dictionary.service.UserService;
import com.square.dictionary.util.AppUtils;

@RestController
@RequestMapping(value = "/auth")
public class AuthRestController {

	@Autowired
	UserService userService;
	
	//Initializing GSON
	private static Gson gson; static {
		gson = new GsonBuilder().setPrettyPrinting().create();
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> login(@RequestParam(required = false) String email, @RequestParam(required = false) String password) {
		User user = new User();
		JsonObject object = new JsonObject();
		object.addProperty(STATUS, false);
		if (email == null || email.trim().equals("") || password == null || password.trim().equals("")) {			
			object.addProperty(MESSAGE, "Email address or password can not be null or empty");
			return new ResponseEntity<String>(gson.toJson(object), HttpStatus.OK);
		} else {
			user = userService.getUserDetails(email, password);
			if (user != null && user.getId() > 0) {
				String token = UUID.randomUUID().toString();
				object = gson.toJsonTree(user).getAsJsonObject();
				AppUtils.getSession().setAttribute(String.valueOf(user.getId()), token);
				JsonObject userJson = (JsonObject) gson.toJsonTree(user);
				userJson.addProperty("token", token);
				return new ResponseEntity<String>(gson.toJson(userJson), HttpStatus.OK);
			} else {
				object.addProperty(MESSAGE, "Either email address or password is incorrect");
				return new ResponseEntity<String>(gson.toJson(object), HttpStatus.OK);	
			}			
		}
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> login() {		
		return AppUtils.getUnsupportedResponse(gson, "Get request");
	}
	//:firstname, :lastname, :email, :password, :gender, :mobile, :address, :city, :state, :country
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> register(@RequestParam(required = false) String firstname, @RequestParam(required = false) String lastname,
			@RequestParam(required = false) String email, 	@RequestParam(required = false) String password,
			@RequestParam(required = false) String gender, 	@RequestParam(required = false) String mobile, 
			@RequestParam(required = false) String address, @RequestParam(required = false) String city, 
			@RequestParam(required = false) String state, 	@RequestParam(required = false) String country) {
		
		User user = new User();
		user.setFirstName(firstname);
		user.setLastName(lastname);
		user.setEmail(email);
		user.setPassword(password);
		user.setGender(gender.trim().equals("1"));
		user.setMobile(mobile);
		user.setAddress(address);
		user.setCity(city);
		user.setState(state);
		user.setCountry(country);
		user.setRegDate(new Date());
		
		JsonObject object = new JsonObject();
		object.addProperty(STATUS, false);
		
		if (user.getEmail() == null) {
			object.addProperty(MESSAGE, "Invalid properties");
			return new ResponseEntity<String>(gson.toJson(object), HttpStatus.OK);
		} else {			
			boolean isRegistered = userService.registerNewUser(user);			
			object.addProperty(STATUS, isRegistered);
			
			if (isRegistered) {
				sendConfirmationMailTo(user);
				object.addProperty(MESSAGE, user.getFirstName() + " " + user.getLastName() + " has been registered successfully. "
						+ "Please verify by clicking on a link sent to your registered email address.");	
			} else {
				object.addProperty(MESSAGE, "Unable to register new user. Please try again");
			}
			
			return new ResponseEntity<String>(gson.toJson(object), HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> register() {		
		return AppUtils.getUnsupportedResponse(gson, "Get request");
	}
	
	private void sendConfirmationMailTo(User user) {
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
			context.register(MailConfiguration.class);
			context.refresh();
			JavaMailSenderImpl mailSender = context.getBean(JavaMailSenderImpl.class);
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			
			MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage);			
			mailMsg.setFrom("noreply@teramatrix.in");
			mailMsg.setTo(user.getEmail().trim());
			mailMsg.setSubject("Confirm your identity");
		    mimeMessage.setContent(getFromTemplate(user), "text/html");		    
			mailSender.send(mimeMessage);	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String getFromTemplate (User user) {
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		velocityEngine.init();
		
		VelocityContext velocityContext = new VelocityContext();
	    velocityContext.put("username", user.getFirstName());
	    velocityContext.put("email", user.getEmail());
	    velocityContext.put("unsubscribe", "Dont want to receive these newsletters?");
	    
	    StringWriter stringWriter = new StringWriter();
	    Template template = velocityEngine.getTemplate("newsletter.html");
	    template.merge(velocityContext, stringWriter);
	    return stringWriter.toString().substring(3);
	}
}