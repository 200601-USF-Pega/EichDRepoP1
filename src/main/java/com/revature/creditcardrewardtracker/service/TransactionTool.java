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

	public Transaction createNewTransaction(int cardID, String date, String category, double total,
			String username) {

		Transaction transaction = new Transaction();
		if (validation.permissionToModifyCard(username, cardID)) {
			transaction.setCardID(cardID);
		} else {
			return transaction;
		}

		transaction.setDate(this.getLDFromStringDate(date));
		transaction.setCategory(category.toUpperCase());
		transaction.setTotal(total);
		transaction.setCashBackTotal(this.calculateCashBack(transaction, username));
		return transaction;

	}
	
	public LocalDate getLDFromStringDate(String date) {
		// getting date from HTML to ld date from Basil Bourque at
		// https://stackoverflow.com/questions/52410740/how-can-i-get-input-type-date-value-from-html-form-into-java-variable-and-sql
		DateTimeFormatter f = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		LocalDate ld = LocalDate.parse(date, f);
		return ld;
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
	
	public double getTotalForDateRange(String username, LocalDate date1, LocalDate date2) {

		List<Transaction> list = tr.listTransactionsForDateRange(username, date1,
				date2);

		return calculateTotalFromList(list);
	}

	public double getTotalCashBack(String username) {
		List<Transaction> list = tr.listTransactions(username);
		return calculateTotalCashBackFromList(list);
	}

	public double getTotalCashBackForDateRange(String username, LocalDate date1, LocalDate date2) {
		List<Transaction> list = tr.listTransactionsForDateRange(username, date1,
				date2);

		return calculateTotalCashBackFromList(list);
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


	

}
