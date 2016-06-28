package com.square.dictionary.controller;

import java.util.List;

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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.square.dictionary.model.Word;
import com.square.dictionary.service.WordService;

@RestController
@RequestMapping(value = "/apis")
public class WebRestController {

	@Autowired
	WordService wordService;
		
	private static final String STATUS = "status";
	private static final String MESSAGE = "message";
	
	//Initializing GSON
	private static Gson gson; static {
		gson = new GsonBuilder().setPrettyPrinting().create();
	}
	
	@RequestMapping(value = "/details/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getDetails(@RequestParam(required = false) String name) {
		Word word = new Word();
		JsonObject object = new JsonObject();
		if (name == null || name.trim().equals("")) {
			object.addProperty(STATUS, false);
			object.addProperty(MESSAGE, "Name that is word's title, can't be null or empty");	
		} else {
			word = wordService.findWordByName(name);
			if (word != null) {				
				object = gson.toJsonTree(word).getAsJsonObject();									
			}			
			object.addProperty(STATUS, true);
			object.addProperty(MESSAGE, (word != null && word.getName() != null) ? "Successful" : "No data found for the word " + name);
		}
		return new ResponseEntity<String>(gson.toJson(word), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/details/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getDetails() {		
		return unsupported();
	}
	
	@RequestMapping(value = "/list/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> listAllWords(@RequestParam(required = false) String category) {
		JsonObject object = new JsonObject();
		if (category == null || category.trim().equals("")) {
			object.addProperty(STATUS, false);
			object.addProperty(MESSAGE, "Category can't be null or empty");	
		} else {
			List<Word> words = wordService.findWordsByCategory(category);
			object.addProperty(STATUS, true);		
			object.addProperty(MESSAGE, (words.isEmpty()) ? "No records found for category " + category : "Successful");
			JsonElement element = gson.toJsonTree(words, new TypeToken<List<Word>>() {}.getType());
			object.add("words", element);
		}
		return new ResponseEntity<String>(gson.toJson(object), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/list/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> listAllWords() {		
		return unsupported();
	}
	
	private ResponseEntity<String> unsupported() {
		JsonObject object = new JsonObject();
		object.addProperty(STATUS, false);		
		object.addProperty(MESSAGE, "Sorry! Get method is not supported by this api");				
		return new ResponseEntity<String>(gson.toJson(object), HttpStatus.OK);
	}
}