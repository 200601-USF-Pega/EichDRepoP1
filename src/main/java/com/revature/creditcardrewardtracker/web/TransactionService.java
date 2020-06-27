package com.revature.creditcardrewardtracker.web;

import java.time.LocalDate;
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

import com.revature.creditcardrewardtracker.dao.ITransactionRepo;
import com.revature.creditcardrewardtracker.dao.TransactionRepoDB;
import com.revature.creditcardrewardtracker.models.Transaction;
import com.revature.creditcardrewardtracker.service.TransactionTool;
import com.revature.creditcardrewardtracker.service.ValidationService;

@Path ("/TransactionService/{username}")
public class TransactionService {
	
	private ITransactionRepo d;
	private TransactionTool t;
	private ValidationService validation;

	
	public TransactionService() {
		d = new TransactionRepoDB();
		t = new TransactionTool();
		validation = new ValidationService();
	}
	
	@POST
	@Path("/{date}/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addTransaction(@PathParam("username") String username,
			@PathParam("date") String date,
			Transaction rTransaction) {
		Transaction transaction = t.createNewTransaction(rTransaction.getCardID(), date, rTransaction.getCategory(), rTransaction.getTotal(), username);
		d.addTransaction(transaction);
		return Response.status(201).build();
	}
	
	@GET
	@Path("/list/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllTransactions(@PathParam("username") String username) {
		return Response.ok((ArrayList<Transaction>)d.listTransactions(username)).build();
	}
	
	@GET
	@Path("/total/category/{category}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategoryTotal(@PathParam("username") String username,
			@PathParam("category") String category) {
		double total = t.getTotalForCategories(username, category);
		return Response.ok(total).build();
	}
	
	@GET
	@Path("/total/cashback/category/{category}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategoryCashBackTotal(@PathParam("username") String username,
			@PathParam("category") String category) {
		double total = t.getTotalCashBackForCategories(username, category);
		return Response.ok(total).build();
	}
	
	@GET
	@Path("/total/card/{cardid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCardTotal(@PathParam("username") String username,
			@PathParam("cardid") int cardid) {
		double total = t.getTotalForCard(username, cardid);
		return Response.ok(total).build();
	}
	
	@GET
	@Path("/total/cashback/card/{cardid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCardCashBackTotal(@PathParam("username") String username,
			@PathParam("cardid") int cardid) {
		double total = t.getTotalCashBackForCard(username, cardid);
		return Response.ok(total).build();
	}
	
	@DELETE
	@Path("/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeTransaction(@PathParam("username") String username,
			int transactionID) {
		if (validation.permissionToModifyTransaction(username, transactionID)) {
			d.deleteTransaction(transactionID);
			return Response.status(201).build();
		}
		return Response.status(401).build();
	}
	
	@PUT
	@Path("/update/date/{date}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateTransactionDate(@PathParam("username") String username,
			@PathParam("date") String date,
			Transaction rTransaction) {
			LocalDate ld = t.getLDFromStringDate(date);
			if (validation.permissionToModifyTransaction(username, rTransaction.getTransactionId())) {
				d.updateTransaction(rTransaction.getTransactionId(), 1, ld);
				return Response.status(201).build();
			}
			return Response.status(401).build();
	}
	
	@PUT
	@Path("/update/category")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateTransactionCategory(@PathParam("username") String username,
			Transaction rTransaction) {
		if (validation.permissionToModifyTransaction(username, rTransaction.getTransactionId())) {
			d.updateTransaction(rTransaction.getTransactionId(), 2, rTransaction.getCategory());
			return Response.status(201).build();
		}
		return Response.status(401).build();
	}
	
	@PUT
	@Path("/update/total")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateTransactionTotal(@PathParam("username") String username,
			Transaction rTransaction) {
		if (validation.permissionToModifyTransaction(username, rTransaction.getTransactionId())) {
			d.updateTransaction(rTransaction.getTransactionId(), 3, rTransaction.getTotal());
			return Response.status(201).build();
		}
		return Response.status(401).build();
	}
	
	@PUT
	@Path("/update/card")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateTransactionCard(@PathParam("username") String username,
			Transaction rTransaction) {
		if (validation.permissionToModifyTransaction(username, rTransaction.getTransactionId())) {
			d.updateTransaction(rTransaction.getTransactionId(), 4, rTransaction.getCardID());
			return Response.status(201).build();
		}
		return Response.status(401).build();
	}
	
}
