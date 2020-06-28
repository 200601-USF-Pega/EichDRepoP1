package com.revature.creditcardrewardtracker.web;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.revature.creditcardrewardtracker.dao.IUserRepo;
import com.revature.creditcardrewardtracker.dao.UserRepoDB;
import com.revature.creditcardrewardtracker.models.User;
import com.revature.creditcardrewardtracker.service.ValidationService;

@Path("/AdminService/{username}")
public class AdminService {

	private IUserRepo d;
	private ValidationService validation;
	private static final Logger log = Logger.getLogger(LogInService.class);


	public AdminService() {
		d = new UserRepoDB();
		validation = new ValidationService();
	}
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsers() {
		return Response.ok((ArrayList<User>)d.getAllUsers()).build();
	}

	@PUT
	@Path("/changePassword")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response changeUserPassword(@PathParam("username") String admin, User user) {
		String username = user.getUsername();
		String newPassword = user.getPassword();
		if (validation.usernameExistsValidation(username)) {
			if (validation.passwordLengthValidation(newPassword)) {
				boolean result = d.changePassword(username, newPassword);
				if (result == true) {
					log.info("User " + user.getUsername() + "'s password updated by admin " + admin+".");
					return Response.status(201).build();
				}
				log.error("Failed to update User " + user.getUsername() + "'s password by admin " + admin+".");
				return Response.status(403).build();
			}
		} 
		log.warn("User " + username + " was NOT found and the password was NOT updated by admin " + admin+".");
		return Response.status(403).build();
	}
	
	@DELETE
	@Path("/deleteuser")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteUserAccount(@PathParam("username") String admin, User user) {
		String username = user.getUsername();
		if (validation.usernameExistsValidation(username)) {
			boolean result = d.deleteUser(username);
			if (result == true) {
				log.warn("User " + user.getUsername() + "'s account deleted by admin " + admin+".");
				return Response.status(201).build();
			} else {
				log.error("Failed to delete User " + user.getUsername() + "'s account by admin " + admin+".");
				return Response.status(403).build();
			}
		} else {
			log.warn("User " + user.getUsername() + "'s account attempted to be deleted by admin " + admin+", but was not found.");
			return Response.status(403).build();
		}
	}
	
	@PUT
	@Path("/promote")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response promoteUserAccount(@PathParam("username") String admin, User user) {
		String username = user.getUsername();
		if (validation.usernameExistsValidation(username)) {
			boolean result = d.promoteAdmin(username);
			if (result == true) {
				log.info("User " + username + " promoted to admin by admin " + admin+".");
				return Response.status(201).build();
			} else {
				log.error("Failed to promote user " + username + " to admin by admin " + admin+".");
				return Response.status(403).build();
			}
		} else {
			log.warn("User " + username + " was NOT found and NOT promoted to admin by admin " + admin+".");
			return Response.status(403).build();
		}
	}

	@PUT
	@Path("/demote")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response demoteUserAccount(@PathParam("username") String admin, User user) {
		String username = user.getUsername();
		if (validation.usernameExistsValidation(username)) {
			boolean result = d.demoteAdmin(username);
			if (result == true) {
				log.info("Admin " + username + " demoted to user by admin " + admin+".");
				return Response.status(201).build();
			} else {
				log.error("Failed to demote admin " + username + " to user by admin " + admin+".");
				return Response.status(403).build();
			}
		} else {
			log.warn("User " + username + " was NOT found and NOT demoted to user by admin " + admin+".");
			return Response.status(403).build();
		}
	}

}
