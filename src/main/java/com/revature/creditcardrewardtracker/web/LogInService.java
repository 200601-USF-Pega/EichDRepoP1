package com.revature.creditcardrewardtracker.web;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.revature.creditcardrewardtracker.dao.IUserRepo;
import com.revature.creditcardrewardtracker.dao.UserRepoDB;
import com.revature.creditcardrewardtracker.service.ValidationService;

//Logger code adapted from from: https://mkyong.com/logging/log4j-hello-world-example/
//and https://www.codejava.net/coding/how-to-configure-log4j-as-logging-mechanism-in-java

@Path("/LogInService")
public class LogInService {
	
	private static final Logger logger = Logger.getLogger(LogInService.class);

	
	private IUserRepo d;
	private ValidationService validation;
	
	public LogInService() {
		d = new UserRepoDB();
		validation = new ValidationService();
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response logInVerification(
			@FormParam("username") String username,
			@FormParam("password") String password) {
		if (validation.usernameExistsValidation(username)) {
			String user = d.checkUser(username, password);
    		logger.info("User " + user + " successfully logged in.");
    		System.out.println(username + " logged in successfully.");
    		/*String uribase = "http:localhost:8080/CreditCardRewardTrackerWeb/addcard.html?username="+username;
    		URI targetURI;
			try {
				targetURI = new URI(uribase);
				return Response.seeOther(targetURI).build();  
			} catch (URISyntaxException e) {
				e.getMessage();
				e.printStackTrace();
			}  		
			*/
    		return Response.status(301).build();
		} else {
			System.out.println("Username not found. Please try again.");
			return Response.status(400).build();
		}
	}
	
	public boolean adminVerification(String username) {
		if(d.checkAdmin(username)){
			logger.info("User " + username + " logged in as an admin.");
			return true;
		}
		return false;
		
	}
	
	
}
