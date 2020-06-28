package com.revature.creditcardrewardtracker.web;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.log4j.Logger;

//ConnectionManager is modeled after Jacob Davis's ConnectionService class

public class ConnectionManager {
	
private static final Logger log = Logger.getLogger(LogInService.class);
	
private static Connection connection;
	
	public static void initialize() {
		try  {		
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(MyProps.url, 
					MyProps.username, MyProps.password);
			log.info("Database connection initialized.");
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
			log.error("Database connection initialization failed.");
		}
	}
	
	public static Connection getConnection() {
		if (connection == null) {
			initialize();
		}
		return connection;
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
