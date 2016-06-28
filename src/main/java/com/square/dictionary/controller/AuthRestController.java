package com.square.dictionary.controller;

import java.util.HashMap;
import java.util.Map;
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

@RestController
@RequestMapping(value = "/auth")
public class AuthRestController {

	@Autowired
	UserService userService;
		
	private static final String STATUS = "status";
	private static final String MESSAGE = "message";
	
	private static final Map<Integer, String> tokens = new HashMap<>();
	
	//Initializing GSON
	private static Gson gson; 
	static {
		gson = new GsonBuilder().setPrettyPrinting().create();
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> login(@RequestParam(required = false) String email, @RequestParam(required = false) String password) {
		User user = new User();
		JsonObject object = new JsonObject();
		if (email == null || email.trim().equals("") || password == null || password.trim().equals("")) {
			object.addProperty(STATUS, false);
			object.addProperty(MESSAGE, "Email address or password can not be null or empty");
			return new ResponseEntity<String>(gson.toJson(object), HttpStatus.OK);
		} else {
			user = userService.loginIfExist(email, password);
			if (user != null && user.getId() > 0) {
				object = gson.toJsonTree(user).getAsJsonObject();
				tokens.put(user.getId(), UUID.randomUUID().toString());
				JsonObject o = (JsonObject) gson.toJsonTree(user);
				o.addProperty("token", tokens.get(user.getId()));
				return new ResponseEntity<String>(gson.toJson(o), HttpStatus.OK);
			}
			object.addProperty(STATUS, true);
			object.addProperty(MESSAGE, "No user exists with the email " + email);
			return new ResponseEntity<String>(gson.toJson(object), HttpStatus.OK);
		}		
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> login() {		
		return unsupported();
	}
		
	private ResponseEntity<String> unsupported() {
		JsonObject object = new JsonObject();
		object.addProperty(STATUS, false);		
		object.addProperty(MESSAGE, "Sorry! Get method is not supported by this resource");				
		return new ResponseEntity<String>(gson.toJson(object), HttpStatus.OK);
	}
}