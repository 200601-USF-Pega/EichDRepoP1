package com.revature.creditcardrewardtracker.web;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.revature.creditcardrewardtracker.dao.CreditCardRepoDB;
import com.revature.creditcardrewardtracker.dao.ICreditCardRepo;
import com.revature.creditcardrewardtracker.models.CreditCard;
import com.revature.creditcardrewardtracker.models.CreditCardReward;
import com.revature.creditcardrewardtracker.service.ValidationService;

@Path("/CalculatorService/{username}")
public class CalculatorService {
	
	private ValidationService validation;
	private ICreditCardRepo ccr;
	
	public CalculatorService() {
		ccr = new CreditCardRepoDB();
		validation = new ValidationService();
	}
	
	@GET
	@Path("/card/{category}/select")
	@Produces(MediaType.TEXT_PLAIN)
	public Response selectBestCard(@PathParam("username") String username,
			@PathParam("category") String category) {				
		if (category.isBlank()) {
			System.out.println("Card Selector category cannot be blank");
			return Response.status(400).build();
		}
		
		String bestCard = null;
		double bestRate = 0.00;
		List<CreditCard> cards = ccr.getCreditCards(username);
	
		for (int i = 0; cards.size() > i; i++) {
			CreditCard tempCard = cards.get(i);
			for (int j = 0; tempCard.getCardCashBackCategories().size() > j; j++) {
				List<CreditCardReward> tempCategoriesList = tempCard.getCardCashBackCategories();
				if (tempCategoriesList.get(j).getCategoryOfCashBack().equalsIgnoreCase(category)) {
					double tempRate = tempCategoriesList.get(j).getPercentageOfCashBack();
					if (tempRate > bestRate) {
						bestRate = tempRate;
						bestCard = tempCard.getCreditCardName();
					}
				}
			}
		}
		String statement = "";
		if (bestRate > 0.00) {
			statement = "The best card for " + category + " is the " + bestCard + " at the rate of " + (bestRate*100) + "%.";
		} else {
			statement = "No cards were found with a cashback category of " + category + ".";
		}
		return Response.ok(statement).build();
	}
	
	@GET
	@Path("/rate/{cardid}/{category}/{total}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response calculatePercentageBack(@PathParam("username") String username,
			@PathParam("cardid") Integer cardNumber,
			@PathParam("category") String category,
			@PathParam("total") double total) {

		if (cardNumber <= 0 || category.isBlank() || total <= 0) {
			System.out.println("Invalid inputs for calculator");
			return Response.status(400).build();
		}
		
		boolean belongsToUser = validation.permissionToModifyCard(username, cardNumber);
		List<CreditCard> cards = ccr.getCreditCards(username);
		String quote = "";
		
		if (belongsToUser == true) {
			for (int i = 0; cards.size() > i; i++) {
				CreditCard tempCard = cards.get(i);
				if (tempCard.getCreditCardID() == cardNumber) {
					double bestRate = 0.00;
					for (int j = 0; tempCard.getCardCashBackCategories().size() > j; j++) {
						List<CreditCardReward> tempCategoriesList = tempCard.getCardCashBackCategories();
						if (tempCategoriesList.get(j).getCategoryOfCashBack().equalsIgnoreCase(category)) {
							double tempRate = tempCategoriesList.get(j).getPercentageOfCashBack();
							if (tempRate > bestRate) {
								bestRate = tempRate;
							}
						}
					}
					double savings = total*bestRate;
					double adjustedPrice = total - savings;
					
					quote = "Using your " + tempCard.getCreditCardName() + " card will save you $" + savings + " for an adjusted total of $" + adjustedPrice + ".";
					return Response.ok(quote).build();
				}
			}
		}
		return Response.ok(quote).build();
	}

}
