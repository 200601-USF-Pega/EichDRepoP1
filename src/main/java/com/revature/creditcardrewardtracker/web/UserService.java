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
import com.revature.creditcardrewardtracker.models.User;
import com.revature.creditcardrewardtracker.service.UserTool;

@Path("/UserService")
public class UserService {
	
private static final Logger logger = Logger.getLogger(LogInService.class);

	
	private IUserRepo d;
	
	public UserService() {
		d = new UserRepoDB();
	}
	
	@POST
	@Path("/newuser")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response newUser(User user) {
		String username = user.getUsername();
		String password = user.getPassword();
		UserTool tool = new UserTool();
		user = tool.createNewUser(username, password, password);
		
		if (user.getUsername()!=null) {
			d.addUser(user);
			logger.info("User " + user + " successfully created.");
    		System.out.println(username + " logged in successfully.");
    		return Response.status(302).build();

		} else {
			System.out.println("User not created. Please try again.");
			return Response.status(403).build();
		}
	}
}
