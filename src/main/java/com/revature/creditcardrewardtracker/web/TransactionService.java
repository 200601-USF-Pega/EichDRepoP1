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

import com.revature.creditcardrewardtracker.dao.ITransactionRepo;
import com.revature.creditcardrewardtracker.dao.TransactionRepoDB;
import com.revature.creditcardrewardtracker.models.Transaction;
import com.revature.creditcardrewardtracker.service.TransactionTool;

@Path ("/TransactionService/{username}")
public class TransactionService {
	
	private ITransactionRepo d;
	private TransactionTool t;

	
	public TransactionService() {
		d = new TransactionRepoDB();
		t = new TransactionTool();
	}
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addTransaction(@PathParam("username") String username,
			@FormParam("cardid") int cardID,
			@FormParam("category") String category,
			@FormParam("purchasedate") String date,
			@FormParam("total") double total) {
		Transaction transaction = t.createNewTransaction(cardID, date, category, total, username);
		d.addTransaction(transaction);
		return Response.status(201).build();
	}
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllTransactions(@PathParam("username") String username) {
		return Response.ok((ArrayList<Transaction>)d.listTransactions(username)).build();
	}
	
}
