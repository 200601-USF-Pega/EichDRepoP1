package com.revature.creditcardrewardtracker.web;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.revature.creditcardrewardtracker.dao.CreditCardRepoDB;
import com.revature.creditcardrewardtracker.dao.ICreditCardRepo;
import com.revature.creditcardrewardtracker.models.CreditCard;
import com.revature.creditcardrewardtracker.service.InputValidationService;
import com.revature.creditcardrewardtracker.service.ValidationService;

@Path("/creditcardservice")
public class CreditCardService {
	
	private String username;
	private ICreditCardRepo d;
	private ValidationService validation;
	private InputValidationService inputValidation;
	
	public CreditCardService(String username) {
		this.username = username;
		d = new CreditCardRepoDB();
		validation = new ValidationService();
	}
	
	public CreditCard createNewCreditCard() {
		
		CreditCardRewardService cashback = new CreditCardRewardService(username);	
		CreditCard card = new CreditCard();
		
		try {
			System.out.println("What is the name of the credit card?");
			String creditCardName = inputValidation.getValidStringInput();
			card.setCreditCardName(creditCardName);
			
			/*System.out.println("What are the last 4 digits of the credit card?");
			int cardID = Integer.parseInt(input.nextLine());
			card.setCreditCardID(cardID);*/
			
			card.setCardCashBackCategories(cashback.createNewCashbackCategory());
			
			card = d.addCreditCard(username, card);
			
			System.out.println(card);
			return card;
		} catch (Exception e) {
			System.out.println("Invalid input type.");
		} 
		return card;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addCreditCard(CreditCard card) {
		d.addCreditCard(username, card);
		return Response.status(201).build();
	}
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCreditCards() {
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
