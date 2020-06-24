package com.revature.creditcardrewardtracker.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.revature.creditcardrewardtracker.dao.CreditCardRepoDB;
import com.revature.creditcardrewardtracker.dao.ICreditCardRepo;
import com.revature.creditcardrewardtracker.dao.ITransactionRepo;
import com.revature.creditcardrewardtracker.dao.TransactionRepoDB;
import com.revature.creditcardrewardtracker.models.CreditCard;
import com.revature.creditcardrewardtracker.models.CreditCardReward;
import com.revature.creditcardrewardtracker.models.Transaction;
import com.revature.creditcardrewardtracker.service.InputValidationService;
import com.revature.creditcardrewardtracker.service.ValidationService;

@Path ("/transactionservice")
public class TransactionService {
	
	private ITransactionRepo d;
	private ValidationService validation;
	private ICreditCardRepo ccr;
	
	public TransactionService() {
		d = new TransactionRepoDB();
		validation = new ValidationService();
		ccr = new CreditCardRepoDB();
	}
	
	@POST
	@Path("/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addTransaction(@PathParam("username") String username,
			@FormParam("cardid") int cardID,
			@FormParam("category") String category,
			@FormParam("purchasedate") String date,
			@FormParam("total") double total) {
		Transaction transaction = this.createNewTransaction(cardID, date, category, total, username);
		d.addTransaction(transaction);
		return Response.status(201).build();
	}
	
	@GET
	@Path("/{username}/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllTransactions(@PathParam("username") String username) {
		return Response.ok((ArrayList<Transaction>)d.listTransactions(username)).build();
	}
	
	
	public Transaction createNewTransaction(int cardID, String htmlDate, String category, double total, String username) {
		
		Transaction transaction = new Transaction();
		boolean belongsToUser = validation.permissionToModifyCard(username, cardID);
		
		if (belongsToUser == true) {
			transaction.setCardID(cardID);
		} else {
			return transaction;
		}
		
		//getting date from HTML to ld date from Basil Bourque at
		//https://stackoverflow.com/questions/52410740/how-can-i-get-input-type-date-value-from-html-form-into-java-variable-and-sql
		DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/uuuu");
		LocalDate ld = LocalDate.parse(htmlDate, f);
		//converting ld to util date from George at 
		//https://stackoverflow.com/questions/33066904/localdate-to-java-util-date-and-vice-versa-simplest-conversion
		java.util.Date date = java.sql.Date.valueOf(ld);
		
		transaction.setDate(date);
		transaction.setCategory(category);
		transaction.setTotal(total);
		transaction.setCashBackTotal(this.calculateCashBack(transaction));
		return transaction;
		
	}
	
	public List<Transaction> getUserTransactions() {
		List<Transaction> list = d.listTransactions(username);
		return list;
	}
	
	public void printUserTransactions() {
		d.printResultSet(username);
	}
	
	public void updateTransaction() {
		d.printResultSet(username);
		System.out.println("Please input the Transaction ID for the transaction to be updated.");
		int id = inputValidation.getValidInt();
		if (validation.permissionToModifyTransaction(username, id) == true) {
			System.out.println("You selected Transaction ID " + id + " to modify. Enter YES to confirm.");
			if (sc.next().equalsIgnoreCase("YES")) {
				System.out.println("What would you like to update?");
				System.out.println("Enter the option for what you'd like to modify: [1] Date; [2] Category; [3] Transaction Total; [4] CardID");
				int option = inputValidation.getValidInt();
				switch (option) {
					case (1) :
						//date
						System.out.println("What date would you like to change it to? Please use the YYYYMMDD format.");
						int intDate = inputValidation.getValidDate();
						java.util.Date newDate = convertIntToDate(intDate);
						d.updateTransaction(id, option, newDate);
						break;
					case (2) :
						//category
						sc.nextLine();
						System.out.println("What category would you like to change it to?");
						String newCat = sc.nextLine().toUpperCase();
						d.updateTransaction(id, option, newCat);
						break;
					case (3) :
						//transaction total
						System.out.println("What is the new Transaction Total?");
						double total = inputValidation.getValidDouble();
						d.updateTransaction(id, option, total);
						break;
					case (4) :
						//card
						System.out.println("What is the new Card ID?");
						System.out.println(ccr.getCreditCards(username));
						int cardID = inputValidation.getValidInt();
						boolean belongsToUser = validation.permissionToModifyCard(username, cardID);
						if (belongsToUser == true) {
							d.updateTransaction(id, option,  cardID);
						}
						break;
					default :
						System.out.println("Invalid input");
				}
				d.printResultSet(username);
			}
		}
	}
	
	public double getTotalForCategories() {
		
		System.out.println("What category would like you pull records from?");
		String category = inputValidation.getValidStringInput().toUpperCase();
		List<Transaction> list = d.listTransactionsForCategory(username, category);
		
		if (list.isEmpty()) {
			System.out.println("Nothing found for that category");
			return 0;
		}
		
		return calculateTotalFromList(list);
	}
	
	public double getTotalForCard() {
		
		System.out.println("What credit card would like you pull records from? Please provide the Card Id.");
		System.out.println(ccr.getCreditCards(username));
		int card = inputValidation.getValidInt();
		boolean belongsToUser = validation.permissionToModifyCard(username, card);
		if (belongsToUser == true) {
			List<Transaction> list = d.listTransactionsForCreditCard(username, card);
			return calculateTotalFromList(list);
		}
		return 0;
	}
	
	public double getTotalForDateRange() {
		
		System.out.println("What is the start date for your date range? Please use YYYYMMDD format.");
		int date1 = inputValidation.getValidDate();
		System.out.println("What is the end date for your date range? Please use YYYYMMDD format.");
		int date2 = inputValidation.getValidDate();
		
		List<Transaction> list = d.listTransactionsForDateRange(username, convertIntToDate(date1), convertIntToDate(date2));
		
		return calculateTotalFromList(list);
	}
	
	public double getTotalCashBack() {
		List<Transaction> list = d.listTransactions(username);
		return calculateTotalCashBackFromList(list);
	}
	
	public double getTotalCashBackForCategories() {
		
		System.out.println("What category would like you pull records from?");
		String category = inputValidation.getValidStringInput().toUpperCase();
		List<Transaction> list = d.listTransactionsForCategory(username, category);
		
		return calculateTotalCashBackFromList(list);
	}
	
	public double getTotalCashBackForCard() {
		
		System.out.println("What credit card would like you pull records from? Please provide the Card Id.");
		System.out.println(ccr.getCreditCards(username));
		
		int card = inputValidation.getValidInt();
		
		boolean belongsToUser = validation.permissionToModifyCard(username, card);
		
		if (belongsToUser == true) {
			List<Transaction> list = d.listTransactionsForCreditCard(username, card);
			return calculateTotalCashBackFromList(list);	
		}
		return 0;
	}
	
	
	public double getTotalCashBackForDateRange() {
		
		System.out.println("What is the start date for your date range? Please use YYYYMMDD format.");
		int date1 = inputValidation.getValidDate();
		System.out.println("What is the end date for your date range? Please use YYYYMMDD format.");
		int date2 = inputValidation.getValidDate();
		
		List<Transaction> list = d.listTransactionsForDateRange(username, convertIntToDate(date1), convertIntToDate(date2));
		
		return calculateTotalCashBackFromList(list);
	}
	
	public void removeTransaction() {
		d.listTransactions(username);
		
		System.out.println("Please input the Transaction ID for the transaction to be deleted.");
		d.printResultSet(username);
		int option = inputValidation.getValidInt();
		
		boolean belongsToUser = validation.permissionToModifyTransaction(username, option);
		
		if (belongsToUser == true) {
			System.out.println("You selected Transaction ID " + option + " to delete. Enter YES to confirm.");
			if (sc.next().equalsIgnoreCase("YES")) {
				d.deleteTransaction(option);
			} else {
				System.out.println("Transaction will not be removed.");
			}
		}
	}
	
	private double calculateCashBack(Transaction transaction) {
		int card = transaction.getCardID();
		double rate = 0.0;
		double everythingRate = 0.0;
		
		double cashBack;
		
		List<CreditCard> cardsOnFile = ccr.getCreditCards(username);
		
		for (CreditCard cc : cardsOnFile) {
			if (cc.getCreditCardID() == card) {
				for (CreditCardReward cat : cc.getCardCashBackCategories()) {
					if (cat.getCategoryOfCashBack().equalsIgnoreCase(transaction.getCategory())) {
						rate = cat.getPercentageOfCashBack();
						break;
					} else if (cat.getCategoryOfCashBack().equalsIgnoreCase("everything")) {
						everythingRate = cat.getPercentageOfCashBack();
						break;
					}
				}
			}
		}
		
		if (rate > everythingRate) {
			cashBack = transaction.getTotal() * rate;
		} else {
			cashBack = transaction.getTotal() * everythingRate;
		}
		
		return cashBack;
	}
	
	private static java.util.Date convertIntToDate(int yyyymmdd) {
		Integer rawDate = (Integer) yyyymmdd;
		String stringDate = rawDate.toString();
		java.util.Date date = null;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		
		try {
			date = format.parse(stringDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return date;
	}
	
	private static double calculateTotalFromList(List<Transaction> list) {
		double total = 0.0;
		
		for (Transaction transaction : list) {
			total += transaction.getTotal();
		}
		
		return total;
	}
	
	private static double calculateTotalCashBackFromList(List<Transaction> list) {
		double total = 0.0;
		
		for (Transaction transaction : list) {
			total += transaction.getCashBackTotal();
		}
		
		return total;
	}
	
}
