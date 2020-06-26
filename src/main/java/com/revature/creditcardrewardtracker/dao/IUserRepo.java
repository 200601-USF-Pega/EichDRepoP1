package com.revature.creditcardrewardtracker.dao;

import java.util.List;

import com.revature.creditcardrewardtracker.models.User;

public interface IUserRepo {
	
	public User addUser(User newUser);
	
	public boolean deleteUser(String username);
	
	public boolean checkUser(String username, String password);
	
	public boolean checkAdmin(String username);
	
	public boolean changePassword(String username, String password);
	
	public boolean promoteAdmin(String username);
	
	public boolean demoteAdmin(String username);
	
	public List<User> getAllUsers();

}
