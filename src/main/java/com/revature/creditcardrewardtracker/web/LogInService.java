package com.revature.creditcardrewardtracker.web;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.revature.creditcardrewardtracker.dao.IUserRepo;
import com.revature.creditcardrewardtracker.dao.UserRepoDB;
import com.revature.creditcardrewardtracker.service.ValidationService;

//Logger code adapted from from: https://mkyong.com/logging/log4j-hello-world-example/
//and https://www.codejava.net/coding/how-to-configure-log4j-as-logging-mechanism-in-java

@Path("loginservice")
public class LogInService {
	
	private static final Logger logger = Logger.getLogger(LogInService.class);

	
	private IUserRepo d;
	private ValidationService validation;
	
	public LogInService() {
		d = new UserRepoDB();
		validation = new ValidationService();
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public Response logInVerification(
			@FormParam("username") String username,
			@FormParam("password") String password) {
		if (validation.usernameExistsValidation(username) == true) {
			String user = d.checkUser(username, password);
    		logger.info("User " + user + " successfully logged in.");
			//return Response.temporaryRedirect(/CreditCardRewardTracker/addcard/+username).build();
    		System.out.println(username + " logged in successfully.");
    		return Response.status(200).build();
		} else {
			System.out.println("Username not found. Please try again.");
			return Response.status(400).build();
		}
	}
	
	public boolean adminVerification(String username) {
		boolean isAdmin = d.checkAdmin(username);
		if(isAdmin==true){
			logger.info("User " + username + " logged in as an admin.");
		}
		return isAdmin;
		
	}
	
	
}
