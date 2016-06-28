package com.square.dictionary.service;

import java.util.List;

import com.square.dictionary.model.Word;

public interface WordService {
	Word findWordById(String id);
	Word findWordByName(String name);
	List<Word> findWordsByCategory(String userId, String category);
}
