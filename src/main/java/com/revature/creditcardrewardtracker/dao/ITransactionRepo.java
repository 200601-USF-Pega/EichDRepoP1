package com.revature.creditcardrewardtracker.dao;

import java.util.List;

import com.revature.creditcardrewardtracker.models.Transaction;

public interface ITransactionRepo {
	
	public void addTransaction(Transaction newTransaction);
		
	public boolean deleteTransaction(int id);
	
	
	//read transactions
	public List<Transaction> listTransactions(String username);
	
	public List<Transaction> listTransactionsForCategory(String username, String category);
	
	public List<Transaction> listTransactionsForCreditCard(String username, int cardID);
	
	public List<Transaction> listTransactionsForDateRange(String username, java.time.LocalDate startDate, java.time.LocalDate endDate);

	public void printResultSet(String username);

	void updateTransaction(int transactionId, int option, Object obj);

}
