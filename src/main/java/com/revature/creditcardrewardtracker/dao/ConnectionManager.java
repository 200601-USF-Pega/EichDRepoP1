package com.revature.creditcardrewardtracker.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionManager {
	
	private Connection connection;
	
	public Connection start() {
		try {
			FileInputStream fis = new FileInputStream("connection.prop");
			Properties p = new Properties();
			p.load(fis);
			connection = DriverManager.getConnection(p.getProperty("hostname"), p.getProperty("username"), p.getProperty("password"));
			
			return connection;
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		} 
		
		return null;
	}
	
	
	//gc will close the connection before the obj is destroyed
	@Override
	public void finalize() {
		try {
			connection.close();
		} catch(Exception e) {
			
		}
	}

}
