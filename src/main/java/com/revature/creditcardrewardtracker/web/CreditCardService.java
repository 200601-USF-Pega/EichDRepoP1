package com.revature.creditcardrewardtracker.web;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.revature.creditcardrewardtracker.dao.CreditCardRepoDB;
import com.revature.creditcardrewardtracker.dao.ICreditCardRepo;
import com.revature.creditcardrewardtracker.models.CreditCard;
import com.revature.creditcardrewardtracker.service.ValidationService;

@Path("/CreditCardService/{username}")
public class CreditCardService {

	private ICreditCardRepo d;
	private ValidationService validation;

	public CreditCardService() {
		d = new CreditCardRepoDB();
		validation = new ValidationService();
	}

	@POST
	@Path("/new")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addCreditCard(@PathParam("username") String username, CreditCard card) {
		d.addCreditCard(username, card);
		// returns http response
		return Response.status(201).build();
	}

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCreditCards(@PathParam("username") String username) {
		return Response.ok((ArrayList<CreditCard>) d.getCreditCards(username)).build();
	}

	@DELETE
	@Path("/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeCreditCard(@PathParam("username") String username, int creditCardID) {
		if (validation.permissionToModifyCard(username, creditCardID)) {
			d.deleteCard(creditCardID);
			return Response.status(201).build();
		}
		return Response.status(401).build();
	}

	@PUT
	@Path("/updatename")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCardName(@PathParam("username") String username, CreditCard card) {
		int id = card.getCreditCardID();
		if (validation.permissionToModifyCard(username, id)) {
			String newName = card.getCreditCardName();
			boolean result = d.updateCard(id, newName);
			if (result == true) {
				System.out.println(id + " updated successfully by user "+username+".");
				return Response.status(201).build();
			} else {
				System.out.println(id + " not updated.");
			}
		}
		return Response.status(401).build();
	}

}
