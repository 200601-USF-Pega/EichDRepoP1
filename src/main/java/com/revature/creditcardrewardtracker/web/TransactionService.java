package com.revature.creditcardrewardtracker.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import org.apache.log4j.Logger;

import com.revature.creditcardrewardtracker.dao.ITransactionRepo;
import com.revature.creditcardrewardtracker.dao.TransactionRepoDB;
import com.revature.creditcardrewardtracker.models.Transaction;
import com.revature.creditcardrewardtracker.service.TransactionTool;
import com.revature.creditcardrewardtracker.service.ValidationService;

@Path("/TransactionService/{username}")
public class TransactionService {

	private ITransactionRepo d;
	private TransactionTool t;
	private ValidationService validation;

	private static final Logger log = Logger.getLogger(LogInService.class);

	public TransactionService() {
		d = new TransactionRepoDB();
		t = new TransactionTool();
		validation = new ValidationService();
	}

	@POST
	@Path("/{date}/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addTransaction(@PathParam("username") String username, @PathParam("date") String date,
			Transaction rTransaction) {
		Transaction transaction = t.createNewTransaction(rTransaction.getCardID(), date, rTransaction.getCategory(),
				rTransaction.getTotal(), username);
		if (transaction.equals(null)) {
			System.out.println("Invalid input");
			return Response.status(401).build();
		}
		d.addTransaction(transaction);
		return Response.status(201).build();
	}

	@GET
	@Path("/list/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllTransactions(@PathParam("username") String username) {
		return Response.ok((ArrayList<Transaction>) d.listTransactions(username)).build();
	}

	@GET
	@Path("/total/category/{category}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategoryTotal(@PathParam("username") String username, @PathParam("category") String category) {
		if (category.isBlank()) {
			return Response.status(400).build();
		}
		double total = t.getTotalForCategories(username, category);
		double totalCashback = t.getTotalCashBackForCategories(username, category);
		ArrayList<Double> results = new ArrayList<Double>();
		results.add(totalCashback);
		results.add(total);
		return Response.ok((ArrayList<Double>) results).build();
	}

	@GET
	@Path("/total/card/{cardid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCardTotal(@PathParam("username") String username, @PathParam("cardid") int cardid) {
		double total = t.getTotalForCard(username, cardid);
		double totalCashback = t.getTotalCashBackForCard(username, cardid);
		ArrayList<Double> results = new ArrayList<Double>();
		results.add(totalCashback);
		results.add(total);
		return Response.ok((ArrayList<Double>) results).build();
	}

	@GET
	@Path("/total/date/{startdate}/{enddate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDateTotal(@PathParam("username") String username, @PathParam("startdate") String startDate,
			@PathParam("enddate") String endDate) {
		double totalCashback = t.getTotalCashBackForDateRange(username, convertHTMLtoLocalDate(startDate),
				convertHTMLtoLocalDate(endDate));
		double total = t.getTotalForDateRange(username, convertHTMLtoLocalDate(startDate),
				convertHTMLtoLocalDate(endDate));
		ArrayList<Double> results = new ArrayList<Double>();
		results.add(totalCashback);
		results.add(total);
		return Response.ok((ArrayList<Double>) results).build();
	}

	@DELETE
	@Path("/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeTransaction(@PathParam("username") String username, int transactionID) {
		if (transactionID <= 0) {
			System.out.println("Transaction ID cannot be null");
			return Response.status(400).build();
		}
		if (validation.permissionToModifyTransaction(username, transactionID)) {
			d.deleteTransaction(transactionID);
			return Response.status(201).build();
		}
		log.info("User " + username + " attempted to remove a transaction they'd don't have access too.");
		return Response.status(405).build();
	}

	@PUT
	@Path("/update/date/{date}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateTransactionDate(@PathParam("username") String username, @PathParam("date") String date,
			Transaction rTransaction) {
		if (date.isBlank()) {
			System.out.println("Invalid date");
			return Response.status(400).build();
		}
		LocalDate ld = t.getLDFromStringDate(date);
		if (validation.permissionToModifyTransaction(username, rTransaction.getTransactionId())) {
			d.updateTransaction(rTransaction.getTransactionId(), 1, ld);
			return Response.status(201).build();
		}
		log.info("User " + username + " attempted to access a transaction they'd don't have access too.");
		return Response.status(405).build();
	}

	@PUT
	@Path("/update/category")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateTransactionCategory(@PathParam("username") String username, Transaction rTransaction) {
		if (rTransaction.getCategory().isBlank()) {
			System.out.println("Invalid category");
			return Response.status(400).build();
		}
		if (validation.permissionToModifyTransaction(username, rTransaction.getTransactionId())) {
			d.updateTransaction(rTransaction.getTransactionId(), 2, rTransaction.getCategory().toUpperCase());
			return Response.status(201).build();
		}
		log.info("User " + username + " attempted to access a transaction they'd don't have access too.");
		return Response.status(405).build();
	}

	@PUT
	@Path("/update/total")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateTransactionTotal(@PathParam("username") String username, Transaction rTransaction) {
		if (rTransaction.getTotal() < 0.01) {
			System.out.println("Invalid amount");
			return Response.status(400).build();
		}
		if (validation.permissionToModifyTransaction(username, rTransaction.getTransactionId())) {
			d.updateTransaction(rTransaction.getTransactionId(), 3, rTransaction.getTotal());
			return Response.status(201).build();
		}
		log.info("User " + username + " attempted to access a transaction they'd don't have access too.");
		return Response.status(405).build();
	}

	@PUT
	@Path("/update/card")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateTransactionCard(@PathParam("username") String username, Transaction rTransaction) {
		if (rTransaction.getCardID() <= 0) {
			System.out.println("Invalid card id");
			return Response.status(400).build();
		}
		if (validation.permissionToModifyTransaction(username, rTransaction.getTransactionId())) {
			d.updateTransaction(rTransaction.getTransactionId(), 4, rTransaction.getCardID());
			return Response.status(201).build();
		}
		log.info("User " + username + " attempted to access a transaction they'd don't have access too.");
		return Response.status(405).build();
	}

	private static LocalDate convertHTMLtoLocalDate(String date) {
		// getting date from HTML to ld date from Basil Bourque at
		// https://stackoverflow.com/questions/52410740/how-can-i-get-input-type-date-value-from-html-form-into-java-variable-and-sql
		DateTimeFormatter f = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		LocalDate ld = LocalDate.parse(date, f);
		return ld;
	}
}
