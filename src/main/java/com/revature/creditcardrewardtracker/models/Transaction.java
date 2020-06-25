package com.revature.creditcardrewardtracker.models;

import java.time.LocalDate;

public class Transaction {
	
	private int cardID;
	private LocalDate date;
	private String category;
	private double total;
	private double cashBackTotal;
	private int transactionId;

	public Transaction() {
		
	}

	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}

	public int getCardID() {
		return cardID;
	}

	public void setCardID(int cardID) {
		this.cardID = cardID;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getCashBackTotal() {
		return cashBackTotal;
	}

	public void setCashBackTotal(double cashBackTotal) {
		this.cashBackTotal = cashBackTotal;
	}

	@Override
	public String toString() {
		return "Transaction [cardID=" + cardID + ", date=" + date + ", category=" + category + ", total=" + total
				+ ", cashBackTotal=" + cashBackTotal + "]";
	}

	public int getTransactionId() {
		return transactionId;
	}
	
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}


	
}
