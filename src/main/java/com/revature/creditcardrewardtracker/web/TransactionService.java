package com.revature.creditcardrewardtracker.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.Consumes;
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

@Path ("/transactionservice/{username}")
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
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addTransaction(Transaction transaction) {
		d.addTransaction(transaction);
		return Response.status(201).build();
	}
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllTransactions(@PathParam("username") String username) {
		return Response.ok((ArrayList<Transaction>)d.listTransactions(username)).build();
	}
	
	
	public Transaction recordNewTransaction() {
		
		Transaction transaction = new Transaction();
		
		System.out.println("Preparing entry for Transaction");
		
		System.out.println("What was the Card Id assocaited with the credit card used?");
		System.out.println(ccr.getCreditCards(username));
		//int cardId = sc.nextInt();
		int cardId = inputValidation.getValidInt();
		boolean belongsToUser = validation.permissionToModifyCard(username, cardId);
		
		if (belongsToUser == true) {
			transaction.setCardID(cardId);
		} else {
			return transaction;
		}
		
		System.out.println("When did the transaction occur? Please use the YYYYMMDD format.");
		//int dateInt = sc.nextInt();
		int dateInt = inputValidation.getValidInt();
		transaction.setDate(convertIntToDate(dateInt));
		
		System.out.println("Which category does the purchase fall under?");
		//will need to print out all the categories in the DB here
		transaction.setCategory(sc.nextLine().toUpperCase());
		
		System.out.println("What was the total for this transaction? Please use 0.00 for the format.");
		//transaction.setTotal(sc.nextDouble());
		double total = inputValidation.getValidDouble();
		transaction.setTotal(total);
		
		transaction.setCashBackTotal(this.calculateCashBack(transaction));
		
		d.addTransaction(transaction);
		
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
