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
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response newUser(
			@FormParam("username") String username,
			@FormParam("password") String password,
			@FormParam("reenter") String passwordVerify) throws URISyntaxException {
		UserTool tool = new UserTool();
		User user = tool.createNewUser(username, password, passwordVerify);
		
		if (user.getUsername()!=null) {
			d.addUser(user);
			logger.info("User " + user + " successfully created.");
    		System.out.println(username + " logged in successfully.");
    		return Response.status(200).build();
    		//String uribase = "http:localhost:8080/CreditCardRewardTrackerWeb/addcard/"+user.getUsername()+"/";
    		//URI targetURI = new URI(uribase);
    		//return Response.temporaryRedirect(targetURI).build();
		} else {
			System.out.println("User not created. Please try again.");
			return Response.status(400).build();
		}
	}
}
