package com.study.service;

import com.study.domain.User;
import com.study.repository.UserRepository;

public class AccountService {
	private static AccountService instance = null;

	
	private AccountService() {}
	
	public static AccountService getInstance() {
		if(instance == null) {
			instance = new AccountService();
		}
		return instance;
	}
	
	
	public boolean isDuplicateUsername(String username) {
		User user = UserRepository.getInstance().findUserByUsername(username);
		return user != null;
	}
	
	public void register(User user) {
		UserRepository.getInstance().saveUser(user);
	}
	
	public User loadUserByUsername(String Username) {
		return UserRepository.getInstance().findUserByUsername(Username);
	}
	
	public boolean checkPassword(User user, String password) {
		return user.getPassword().equals(password);
	}
}
