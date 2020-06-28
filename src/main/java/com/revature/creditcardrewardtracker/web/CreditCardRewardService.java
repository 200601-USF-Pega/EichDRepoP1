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

import com.revature.creditcardrewardtracker.dao.CreditCardRewardsRepoDB;
import com.revature.creditcardrewardtracker.dao.ICreditCardRewardsRepo;
import com.revature.creditcardrewardtracker.models.CreditCardReward;
import com.revature.creditcardrewardtracker.service.ValidationService;

@Path("/CreditCardRewardService/{username}/{cardid}")
public class CreditCardRewardService {
	
	private ICreditCardRewardsRepo ccrr;
	private ValidationService validation;


	public CreditCardRewardService() {
		ccrr = new CreditCardRewardsRepoDB();
		validation = new ValidationService();
	}
	
	@POST
	@Path("/new")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addCreditCardReward(@PathParam("cardid") Integer cardId,
			@PathParam("username") String username,
			CreditCardReward reward) {
		if (reward.getCategoryOfCashBack().isBlank()) {
			System.out.println("Reward name cannot be blank");
			return Response.status(400).build();
		} else if (reward.getPercentageOfCashBack() <= 0){
			System.out.println("Reward rate must be greater than 0%");
			return Response.status(400).build();
		}
		if (validation.permissionToModifyCard(username, cardId)) {
			ccrr.addCashBackCategory(cardId, reward);
			return Response.status(201).build();
		}
		return Response.status(405).build();
		
	}
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCreditRewards(@PathParam("username") String username, 
			@PathParam("cardid") Integer cardId) {
		if (validation.permissionToModifyCard(username, cardId)) {
			return Response.ok((ArrayList<CreditCardReward>)ccrr.getCashBackCategories(cardId)).build();
		} 
		return Response.status(405).build();
	}
	
	@PUT
	@Path("/update/name")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCreditCardRewardName(@PathParam("cardid") Integer cardId, 
			@PathParam("username") String username,
			CreditCardReward reward) {
		if (reward.getCategoryOfCashBack().isBlank()) {
			System.out.println("Reward name cannot be blank");
			return Response.status(400).build();
		}
		if (validation.permissionToModifyCard(username, cardId)) {
			ccrr.updateCashBackCategory(reward.getCategoryID(), 0, reward.getCategoryOfCashBack());
			return Response.status(201).build();
		}
		return Response.status(405).build();
	}
	
	@PUT
	@Path("/update/rate")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCreditCardRewardRate(@PathParam("cardid") Integer cardId, 
			@PathParam("username") String username,
			CreditCardReward reward) {
		if (reward.getPercentageOfCashBack() <= 0){
			System.out.println("Reward rate must be greater than 0%");
			return Response.status(400).build();
		}
		if (validation.permissionToModifyCard(username, cardId)) {
			ccrr.updateCashBackCategory(reward.getCategoryID(), 1, reward.getPercentageOfCashBack());
			return Response.status(201).build();
		}
		return Response.status(405).build();
	}
	
	@DELETE
	@Path("/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeCreditcardReward(@PathParam("cardid") Integer cardId, 
			@PathParam("username") String username,
			int categoryId) {
		if (categoryId <= 0) {
			System.out.println("Category ID cannot be null");
			return Response.status(400).build();
		}
		if (validation.permissionToModifyCard(username, cardId)) {
			ccrr.deleteCashBackCategory(categoryId);
			return Response.status(201).build();
		}
		return Response.status(405).build();
	}
	
	


}
