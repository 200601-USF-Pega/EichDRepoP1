package com.revature.creditcardrewardtracker.web;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.revature.creditcardrewardtracker.dao.CreditCardRepoDB;
import com.revature.creditcardrewardtracker.dao.ICreditCardRepo;
import com.revature.creditcardrewardtracker.models.CreditCard;
import com.revature.creditcardrewardtracker.models.CreditCardReward;
import com.revature.creditcardrewardtracker.service.InputValidationService;
import com.revature.creditcardrewardtracker.service.ValidationService;

@Path("/creditcardservice/{username}")
public class CreditCardService {
	
	private ICreditCardRepo d;
	private ValidationService validation;
	private InputValidationService inputValidation;
	
	public CreditCardService() {
		d = new CreditCardRepoDB();
		validation = new ValidationService();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addCreditCard(@PathParam("username") String username, 
			@FormParam("cardname") String cardName,
			@FormParam("category") String category,
			@FormParam("categoryrate") double rate) {
		//build POJO to add to DB
		CreditCard card = new CreditCard();
		card.setCreditCardName(cardName);
		List<CreditCardReward> rewards = new ArrayList<CreditCardReward>();
		CreditCardReward reward = new CreditCardReward();
		reward.setCategoryOfCashBack(category);
		reward.setPercentageOfCashBack(rate);
		rewards.add(reward);
		card.setCardCashBackCategories(rewards);
		//add POJO to DB
		d.addCreditCard(username, card);
		//returns http response
		return Response.status(201).build();
	}
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCreditCards(@PathParam("username") String username) {
		return Response.ok((ArrayList<CreditCard>)d.getCreditCards(username)).build();
	}
	
	public void deleteCreditCard() {
		System.out.println("Please enter the Card ID for the credit card you wish to delete.");
		
		List<CreditCard> cards = d.getCreditCards(username);
		for (CreditCard c : cards) {
			System.out.println(c.stringNameAndId());
		}
		
		int id = inputValidation.getValidInt();
		
		boolean belongsToUser = validation.permissionToModifyCard(username, id);
		
		if (belongsToUser == true) {
			boolean result = d.deleteCard(id);
			if (result == true) {
				System.out.println(id + " deleted successfully.");
			} else {
				System.out.println(id + " not deleted.");
			}
		}
	}
	
	public void updateCardName() {
		System.out.println("Please enter the Card ID for the credit card you wish to modify.");
		
		List<CreditCard> cards = d.getCreditCards(username);
		for (CreditCard c : cards) {
			System.out.println(c.stringNameAndId());
		}
		
		int id = inputValidation.getValidInt();
		
		boolean belongsToUser = validation.permissionToModifyCard(username, id);
		
		if (belongsToUser == true) {
			System.out.println("What would you like to rename this card?");
			String newName = inputValidation.getValidStringInput();
			
			boolean result = d.updateCard(id, newName);
			if (result == true) {
				System.out.println(id + " updated successfully.");
			} else {
				System.out.println(id + " not updated.");
			} 
		}
	}

}
