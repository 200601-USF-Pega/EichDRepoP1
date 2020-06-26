package com.revature.creditcardrewardtracker.web;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.revature.creditcardrewardtracker.dao.IUserRepo;
import com.revature.creditcardrewardtracker.dao.UserRepoDB;
import com.revature.creditcardrewardtracker.models.User;
import com.revature.creditcardrewardtracker.service.ValidationService;

@Path("/AdminService/{username}")
public class AdminService {

	private IUserRepo d;
	private ValidationService validation;

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
	public Response changeUserPassword(User user) {
		String username = user.getUsername();
		String newPassword = user.getPassword();
		if (validation.usernameExistsValidation(username)) {
			if (validation.passwordLengthValidation(newPassword)) {
				boolean result = d.changePassword(username, newPassword);
				if (result == true) {
					System.out.println(username + " password's updated.");
					return Response.status(201).build();
				}
				System.out.println(username + " password not changed.");
				return Response.status(403).build();
			}
		} 
		System.out.println("User " + username + " not found.");
		return Response.status(403).build();
	}
	
	@DELETE
	@Path("/deleteuser")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteUserAccount(User user) {
		String username = user.getUsername();
		if (validation.usernameExistsValidation(username)) {
			boolean result = d.deleteUser(username);
			if (result == true) {
				System.out.println(username + "'s account deleted.");
				return Response.status(201).build();
			} else {
				System.out.println(username + "'s account not deleted.");
				return Response.status(403).build();
			}
		} else {
			System.out.println("User " + username + " not found.");
			return Response.status(403).build();
		}
	}
	
	@PUT
	@Path("/promote")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response promoteUserAccount(User user) {
		String username = user.getUsername();
		if (validation.usernameExistsValidation(username)) {
			boolean result = d.promoteAdmin(username);
			if (result == true) {
				System.out.println(username + " promoted to admin successfully.");
				return Response.status(201).build();
			} else {
				System.out.println(username + " not promoted to admin.");
				return Response.status(403).build();
			}
		} else {
			System.out.println("User " + username + " not found.");
			return Response.status(403).build();
		}
	}

	@PUT
	@Path("/demote")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response demoteUserAccount(User user) {
		String username = user.getUsername();
		if (validation.usernameExistsValidation(username)) {
			boolean result = d.demoteAdmin(username);
			if (result == true) {
				System.out.println(username + " demoted to standard account successfully.");
				return Response.status(201).build();
			} else {
				System.out.println(username + " not demoted to standard account.");
				return Response.status(403).build();
			}
		} else {
			System.out.println("User " + username + " not found.");
			return Response.status(403).build();
		}
	}

}
