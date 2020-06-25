package com.revature.creditcardrewardtracker.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.revature.creditcardrewardtracker.dao.CreditCardRepoDB;
import com.revature.creditcardrewardtracker.dao.ICreditCardRepo;
import com.revature.creditcardrewardtracker.dao.ITransactionRepo;
import com.revature.creditcardrewardtracker.dao.TransactionRepoDB;
import com.revature.creditcardrewardtracker.models.CreditCard;
import com.revature.creditcardrewardtracker.models.CreditCardReward;
import com.revature.creditcardrewardtracker.models.Transaction;

public class TransactionTool {
	
	private ValidationService validation;
	private ICreditCardRepo ccr;
	private ITransactionRepo tr;

	public TransactionTool() {
		validation = new ValidationService();
		ccr = new CreditCardRepoDB();
		tr = new TransactionRepoDB();
	}

	public Transaction createNewTransaction(int cardID, String htmlDate, String category, double total,
			String username) {

		Transaction transaction = new Transaction();
		if (validation.permissionToModifyCard(username, cardID)) {
			transaction.setCardID(cardID);
		} else {
			return transaction;
		}

		// getting date from HTML to ld date from Basil Bourque at
		// https://stackoverflow.com/questions/52410740/how-can-i-get-input-type-date-value-from-html-form-into-java-variable-and-sql
		DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/uuuu");
		LocalDate ld = LocalDate.parse(htmlDate, f);
		// converting ld to util date from George at
		// https://stackoverflow.com/questions/33066904/localdate-to-java-util-date-and-vice-versa-simplest-conversion
		java.util.Date date = java.sql.Date.valueOf(ld);

		transaction.setDate(date);
		transaction.setCategory(category.toUpperCase());
		transaction.setTotal(total);
		transaction.setCashBackTotal(this.calculateCashBack(transaction, username));
		return transaction;

	}
	
	public double getTotalForCategories(String username, String category) {
		List<Transaction> list = tr.listTransactionsForCategory(username, category.toUpperCase());
		if (list.isEmpty()) {
			System.out.println("Nothing found for that category");
			return 0;
		}
		return calculateTotalFromList(list);
	}
	
	public double getTotalCashBackForCategories(String username, String category) {
		List<Transaction> list = tr.listTransactionsForCategory(username, category.toUpperCase());
		if (list.isEmpty()) {
			System.out.println("Nothing found for that category");
			return 0;
		}
		return calculateTotalCashBackFromList(list);
	}
	
	public double getTotalForCard(String username, int cardid) {
		if (validation.permissionToModifyCard(username, cardid)) {
			List<Transaction> list = tr.listTransactionsForCreditCard(username, cardid);
			return calculateTotalFromList(list);
		}
		return 0;
	}
	
	public double getTotalCashBackForCard(String username, int cardid) {
		if (validation.permissionToModifyCard(username, cardid)) {
			List<Transaction> list = tr.listTransactionsForCreditCard(username, cardid);
			return calculateTotalCashBackFromList(list);
		}
		return 0;
	}
	
	private double calculateCashBack(Transaction transaction, String username) {
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

	/*
	public List<Transaction> getUserTransactions() {
		List<Transaction> list = d.listTransactions(username);
		return list;
	}

	public void printUserTransactions() {
		d.printResultSet(username);
	}

	public void updateTransaction() {
		int id = null;
		if (validation.permissionToModifyTransaction(username, id) == true) {
				int option = null;
				switch (option) {
				case (1):
					// date
					System.out.println("What date would you like to change it to? Please use the YYYYMMDD format.");
					int intDate = null;
					java.util.Date newDate = convertIntToDate(intDate);
					d.updateTransaction(id, option, newDate);
					break;
				case (2):
					// category
					sc.nextLine();
					System.out.println("What category would you like to change it to?");
					String newCat = sc.nextLine().toUpperCase();
					d.updateTransaction(id, option, newCat);
					break;
				case (3):
					// transaction total
					System.out.println("What is the new Transaction Total?");
					double total = inputValidation.getValidDouble();
					d.updateTransaction(id, option, total);
					break;
				case (4):
					// card
					System.out.println("What is the new Card ID?");
					System.out.println(ccr.getCreditCards(username));
					int cardID = inputValidation.getValidInt();
					boolean belongsToUser = validation.permissionToModifyCard(username, cardID);
					if (belongsToUser == true) {
						d.updateTransaction(id, option, cardID);
					}
					break;
				default:
					System.out.println("Invalid input");
				}
				d.printResultSet(username);
			}
		}
	}

	public double getTotalForDateRange() {

		System.out.println("What is the start date for your date range? Please use YYYYMMDD format.");
		int date1 = inputValidation.getValidDate();
		System.out.println("What is the end date for your date range? Please use YYYYMMDD format.");
		int date2 = inputValidation.getValidDate();

		List<Transaction> list = d.listTransactionsForDateRange(username, convertIntToDate(date1),
				convertIntToDate(date2));

		return calculateTotalFromList(list);
	}

	public double getTotalCashBack() {
		List<Transaction> list = d.listTransactions(username);
		return calculateTotalCashBackFromList(list);
	}

	public double getTotalCashBackForDateRange() {

		System.out.println("What is the start date for your date range? Please use YYYYMMDD format.");
		int date1 = inputValidation.getValidDate();
		System.out.println("What is the end date for your date range? Please use YYYYMMDD format.");
		int date2 = inputValidation.getValidDate();

		List<Transaction> list = d.listTransactionsForDateRange(username, convertIntToDate(date1),
				convertIntToDate(date2));

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

	
	*/

}
