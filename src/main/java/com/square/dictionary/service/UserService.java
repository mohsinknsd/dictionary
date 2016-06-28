package com.square.dictionary.service;

import com.square.dictionary.model.User;

public interface UserService {
	User loginIfExist(String email, String password);
}