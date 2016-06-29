package com.square.dictionary.service;

import com.square.dictionary.model.User;

public interface UserService {
	User getUserDetails(String email, String password);
	boolean registerNewUser(User user);
}