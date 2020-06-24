package com.revature.creditcardrewardtracker.dao;

import java.util.List;

import com.revature.creditcardrewardtracker.models.CreditCardReward;

public interface ICreditCardRewardsRepo {

	public boolean addCashBackCategory(int cardId, String category, double percentageback);
	
	public void addCashBackCategory(int cardId, CreditCardReward reward);
	
	public boolean deleteCashBackCategory(int categoryId);
	
	public boolean updateCashBackCategory(int categoryId, int option, Object obj);
	
	public List<CreditCardReward> getCashBackCategories(int cardId);

	void printCashBackCategories(int cardId);
}
