package com.revature.creditcardrewardtracker.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.creditcardrewardtracker.models.User;

public class UserRepoDB implements IUserRepo {

	Connection connection;
	
	public UserRepoDB(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public User addUser(User newUser) {
		try {
			Statement userStatement = connection.createStatement();
			userStatement.executeUpdate("INSERT INTO users VALUES ('" + newUser.getUsername() 
				+ "', '" + newUser.getPassword() + "', '" + newUser.isAdmin() + "');");
			return newUser;
		} catch (SQLException e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> getAllUsers() {
		
		List<String> listOfUsernames = new ArrayList<String>();
		try {
			Statement s = connection.createStatement();
			s.executeQuery("SELECT username FROM users;");

			ResultSet rs = s.getResultSet();
			while (rs.next()) {
				listOfUsernames.add(rs.getString("username"));
			}
			
			return listOfUsernames;
		} catch (SQLException e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
		
	@Override
	public String checkUser(String username, String password) {
		try {
			Statement userStatement = connection.createStatement();
			userStatement.executeQuery("SELECT * FROM users;");
			
			ResultSet rs = userStatement.getResultSet();
			while (rs.next()) {
				if (rs.getString("username").equalsIgnoreCase(username)) {
					if (rs.getString("password").equals(password)) {
						return username;
					} else {
						System.out.println("Wrong password");
						return null;
					}
				}
			}
			System.out.println("Username not found.");
		} catch (SQLException e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean checkAdmin(String username) {
		try {
			Statement s = connection.createStatement();
			s.executeQuery("SELECT * FROM users;");
			
			ResultSet rs = s.getResultSet();
			while (rs.next()) {
				if (rs.getString("username").equalsIgnoreCase(username)) {
					return rs.getBoolean("isadmin");
				}
			}
		} catch (SQLException e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		
		System.out.println("User not found.");
		return false;
	}
	
	public boolean changePassword(String username, String password) {
		try {
			Statement s = connection.createStatement();
			s.executeUpdate("UPDATE Users "
					+ "SET password = '" + password +
					"' WHERE username = '" + username + "';");
			return true;
		} catch (SQLException e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		
		System.out.println("User not found. Please try again.");
		return false;
	}
	
	public boolean promoteAdmin(String username) {
		try {
			Statement s = connection.createStatement();
			s.executeUpdate("UPDATE Users "
					+ "SET isadmin = '" + true +
					"' WHERE username = '" + username + "';");
			
			return true;
		} catch (SQLException e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		
		System.out.println("User not found. Please try again.");
		return false;
	}
	
	public boolean demoteAdmin(String username) {
		try {
			Statement s = connection.createStatement();
			s.executeUpdate("UPDATE Users "
					+ "SET isadmin = '" + false +
					"' WHERE username = '" + username + "';");
			
			return true;
		} catch (SQLException e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		
		System.out.println("User not found. Please try again.");
		return false;
	}

	@Override
	public boolean deleteUser(String username) {
		try {
			Statement s = connection.createStatement();
			s.execute("DELETE FROM users WHERE username = '" + username + "';");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("User not found. Please try again.");
		return false;
	}
}
