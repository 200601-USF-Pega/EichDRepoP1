package com.revature.creditcardrewardtracker.web;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.revature.creditcardrewardtracker.dao.CreditCardRepoDB;
import com.revature.creditcardrewardtracker.dao.CreditCardRewardsRepoDB;
import com.revature.creditcardrewardtracker.dao.ICreditCardRepo;
import com.revature.creditcardrewardtracker.dao.ICreditCardRewardsRepo;
import com.revature.creditcardrewardtracker.models.CreditCardReward;
import com.revature.creditcardrewardtracker.models.CreditCard;
import com.revature.creditcardrewardtracker.service.ValidationService;

@Path("/creditcardrewardservice")
public class CreditCardRewardService {
	
	private String username;
	private ICreditCardRepo d;
	private ICreditCardRewardsRepo ccrr;
	private ValidationService validation;


	public CreditCardRewardService(String username) {
		this.username = username;
		ccrr = new CreditCardRewardsRepoDB();
		d = new CreditCardRepoDB();
		validation = new ValidationService();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addCreditCardReward(int cardId, CreditCardReward reward) {
		ccrr.addCashBackCategory(cardId, reward);
		return Response.status(201).build();
	}
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCreditRewards(int cardId) {
		return Response.ok((ArrayList<CreditCardReward>)ccrr.getCashBackCategories(cardId)).build();
	}
	
	
	public List<CreditCardReward> createNewCashbackCategory() {
		
		//Scanner cashScan = new Scanner(System.in);
		List<CreditCardReward> categories = new ArrayList<CreditCardReward>();
		
		System.out.println("What are the cash back categories and their percentage back? Enter done when complete");
		
		
		try {
			String scanResult;
			do {
				CreditCardReward category = new CreditCardReward();
				
				System.out.println("What is the name of the category?");
				scanResult = inputValidation.getValidStringInput();
				if (scanResult.equalsIgnoreCase("done")) {
					break;
				}
				category.setCategoryOfCashBack(scanResult);
				
				System.out.println("What is the cash back rate?");
				System.out.println("Please provide the cash back rate in 0.00 format (i.e. 5% would be 0.05).");
				category.setPercentageOfCashBack(inputValidation.getValidDouble());
				
				categories.add(category);
				
			} while (!scanResult.equalsIgnoreCase("done"));
			
			return categories;
		} catch (Exception e) {
			System.out.println("Invalid input type.");
		}
		
		return categories;
		
	}
	
	public void addCashbackCategory() {
		int id = this.identifyCreditCard();
		
		System.out.println("What is the name of the category?");
		String category = inputValidation.getValidStringInput();

		
		System.out.println("What is the cash back rate?");
		System.out.println("Please provide the cash back rate in 0.00 format (i.e. 5% would be 0.05).");
		double percentageback = inputValidation.getValidDouble();
		
		boolean result = ccrr.addCashBackCategory(id, category, percentageback);
		
		if (result == true) {
			System.out.println("Category " + category + " successfully added to card " + id + ".");
		} else {
			System.out.println("Category failed to be added.");
		}
		
	}
	
	public void updateCashbackCategory() {
		int category = this.identifyCategory();
		
		System.out.println("Which attribute of this category would you like to modify?");
		System.out.println("Enter 0 for category or 1 for percentage of cash back.");
		int option = inputValidation.getValidInt();
				
		Object obj;
		boolean result = false;
		
		switch (option) {
		case (0) :
			System.out.println("Please enter the new category name.");
			obj = inputValidation.getValidStringInput();
			result = ccrr.updateCashBackCategory(category, option, obj);
			break;
		case (1) :
			System.out.println("Please enter the new cash back rate as a decimal. For example, 5% would 0.05.");
			obj = inputValidation.getValidDouble();
			result = ccrr.updateCashBackCategory(category, option, obj);
			break;
		default :
			System.out.println("Invalid selection.");
		}
		
		if (result == true) {
			System.out.println("Category " + category + " successfully updated.");
		} else {
			System.out.println("Category failed to be updated.");
		}  
	}
	
	public void deleteCashbackCategory() {
		int category = this.identifyCategory();
		
		boolean result = ccrr.deleteCashBackCategory(category);
		if (result == true) {
			System.out.println("Category " + category + " was successfully removed from database.");
		} else {
			System.out.println("Category failed to be removed.");
		}
	}
	
	public void printCashBackCategories() {
		int id = this.identifyCreditCard();
		List<CreditCardReward> list = ccrr.getCashBackCategories(id);
		System.out.println(list);
	}
	
	private int identifyCreditCard() {
		boolean belongsToUser = false;
		while (belongsToUser == false) {
			System.out.println("Which card? Please enter the Card ID.");
			
			List<CreditCard> cards = d.getCreditCards(username);
			for (CreditCard c : cards) {
				System.out.println(c.stringNameAndId());
			}
			
			int id = inputValidation.getValidInt();
			belongsToUser = validation.permissionToModifyCard(username, id);
			
			if (belongsToUser == true) {
				return id;
			}
		}
		return -1;
	}
	
	private int identifyCategory() {
		int id = this.identifyCreditCard();
		
		System.out.println("Which category would you like to modify? Please enter the Reward Id number.");
		//System.out.println(ccrr.getCashBackCategories(id));
		ccrr.printCashBackCategories(id);
		int categoryId = inputValidation.getValidInt();
		return categoryId;
	}

}
