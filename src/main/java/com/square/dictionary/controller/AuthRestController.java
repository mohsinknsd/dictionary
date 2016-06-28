package com.square.dictionary.controller;

import static com.square.dictionary.model.Constants.MESSAGE;
import static com.square.dictionary.model.Constants.STATUS;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
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
}