package com.square.dictionary.util;

import static com.square.dictionary.model.Constants.MESSAGE;
import static com.square.dictionary.model.Constants.STATUS;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class AppUtils {
	
	private static ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	
	static HttpSession session = null; static {
		session = attrs.getRequest().getSession();
	}
	
	public static HttpSession getSession() {
		return session;
	}
	
	public static ResponseEntity<String> getUnsupportedResponse(Gson gson, String method) {
		JsonObject object = new JsonObject();
		object.addProperty(STATUS, false);		
		object.addProperty(MESSAGE, "Sorry! " + method + " is not supported by this resource");				
		return new ResponseEntity<String>(gson.toJson(object), HttpStatus.BAD_REQUEST);
	}
}