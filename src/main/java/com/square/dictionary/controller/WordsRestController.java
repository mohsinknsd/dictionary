package com.square.dictionary.controller;

import static com.square.dictionary.model.Constants.MESSAGE;
import static com.square.dictionary.model.Constants.STATUS;

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
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.square.dictionary.model.Word;
import com.square.dictionary.service.WordService;
import com.square.dictionary.util.AppUtils;

@RestController
@RequestMapping(value = "/apis")
public class WordsRestController {

	@Autowired
	WordService wordService;

	//Initializing GSON
	private static Gson gson; static {
		gson = new GsonBuilder().setPrettyPrinting().create();
	}

	@RequestMapping(value = "/details", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getDetails(@RequestParam(required = false) String userId, 
			@RequestParam(required = false) String token, @RequestParam(required = false) String name) {
		
		JsonObject object = new JsonObject();
		object.addProperty(STATUS, false);
		
		//Security Check
		Object t = AppUtils.getSession().getAttribute(userId); 
		if (t == null || !t.toString().equals(token)) {			
			object.addProperty(MESSAGE, "Unauthorized user");
			return new ResponseEntity<String>(gson.toJson(object), HttpStatus.UNAUTHORIZED);
		}
		
		//Request Validations
		if (name == null || name.trim().equals("")) {			
			object.addProperty(MESSAGE, "Name that is word's title, can't be null or empty");
			return new ResponseEntity<String>(gson.toJson(object), HttpStatus.OK);
		}
		
		Word word = new Word();
		word = wordService.findWordByName(name);
		if (word != null) {				
			object = gson.toJsonTree(word).getAsJsonObject();									
		}			
		object.addProperty(STATUS, true);
		object.addProperty(MESSAGE, (word != null && word.getName() != null) ? "Successful" : "No data found for the word " + name);
		return new ResponseEntity<String>(gson.toJson(word), HttpStatus.OK);
	}

	@RequestMapping(value = "/details", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getDetails() {		
		return AppUtils.getUnsupportedResponse(gson, "Get request");
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> listAllWords(@RequestParam(required = false) String userId, 
			@RequestParam(required = false) String token, @RequestParam(required = false) String category) {
		JsonObject object = new JsonObject();
		object.addProperty(STATUS, false);

		//Security Check
		Object t = AppUtils.getSession().getAttribute(userId); 
		if (t == null || !t.toString().equals(token)) {			
			object.addProperty(MESSAGE, "Unauthorized user");
			return new ResponseEntity<String>(gson.toJson(object), HttpStatus.UNAUTHORIZED);
		}

		//Request Validations
		if (category == null || category.trim().equals("")) {
			object.addProperty(MESSAGE, "Category can't be null or empty");	
			return new ResponseEntity<String>(gson.toJson(object), HttpStatus.OK);
		} 

		//Publishing Results
		List<Word> words = wordService.findWordsByCategory(userId, category);
		object.addProperty(STATUS, true);		
		object.addProperty(MESSAGE, (words.isEmpty()) ? "No records found for category " + category : "Successful");		
		object.add("words", gson.toJsonTree(words, new TypeToken<List<Word>>() {}.getType()));
		return new ResponseEntity<String>(gson.toJson(object), HttpStatus.OK);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> listAllWords() {		
		return AppUtils.getUnsupportedResponse(gson, "Get request");
	}
}