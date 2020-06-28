package com.revature.creditcardrewardtracker.web;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.revature.creditcardrewardtracker.dao.IUserRepo;
import com.revature.creditcardrewardtracker.dao.UserRepoDB;
import com.revature.creditcardrewardtracker.models.User;
import com.revature.creditcardrewardtracker.service.ValidationService;

//Logger code adapted from from: https://mkyong.com/logging/log4j-hello-world-example/
//and https://www.codejava.net/coding/how-to-configure-log4j-as-logging-mechanism-in-java

@Path("/LogInService")
public class LogInService {
	
	private static final Logger log = Logger.getLogger(LogInService.class);

	
	private IUserRepo d;
	private ValidationService validation;
	
	public LogInService() {
		d = new UserRepoDB();
		validation = new ValidationService();
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response logInVerification(User user) {
		String username = user.getUsername();
		String password = user.getPassword();
		if (validation.usernameExistsValidation(username)) {
			if (d.checkUser(username, password)) {
				if (this.adminVerification(username)) {
					return Response.status(303).build();
				}
				log.info("User " + user.getUsername() + " successfully logged in.");
				return Response.status(302).build();
			}
		} else {
			System.out.println("Username" + username + " not found. Please try again.");
		}
		return Response.status(403).build();
	}
	
	public boolean adminVerification(String username) {
		if(d.checkAdmin(username)){
			log.info("User " + username + " logged in as an admin.");
			return true;
		}
		return false;
	}
	
	
}
