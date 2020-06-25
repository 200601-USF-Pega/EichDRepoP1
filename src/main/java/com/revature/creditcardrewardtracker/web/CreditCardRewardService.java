package com.revature.creditcardrewardtracker.web;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.revature.creditcardrewardtracker.dao.CreditCardRewardsRepoDB;
import com.revature.creditcardrewardtracker.dao.ICreditCardRewardsRepo;
import com.revature.creditcardrewardtracker.models.CreditCardReward;
import com.revature.creditcardrewardtracker.service.CreditCardRewardTool;
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
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addCreditCardReward(@PathParam("cardid") Integer cardId,
			@PathParam("username") String username,
			@FormParam("category") String category,
			@FormParam("categoryrate") double rate) {
		if (validation.permissionToModifyCard(username, cardId)) {
			CreditCardRewardTool t = new CreditCardRewardTool();
			CreditCardReward reward = t.createNewCashbackCategory(category, rate);
			ccrr.addCashBackCategory(cardId, reward);
			return Response.status(201).build();
		}
		return Response.status(400).build();
		
	}
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCreditRewards(@PathParam("cardid") Integer cardId, 
			@PathParam("username") String username) {
		if (validation.permissionToModifyCard(username, cardId)) {
			return Response.ok((ArrayList<CreditCardReward>)ccrr.getCashBackCategories(cardId)).build();
		} 
		return Response.status(400).build();
	}
	


}
