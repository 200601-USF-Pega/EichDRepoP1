package com.revature.creditcardrewardtracker.dao;

import java.util.List;

import com.revature.creditcardrewardtracker.models.CategoryCashBack;

public interface ICreditCardRewardsRepo {

	public boolean addCashBackCategory(int cardId, String category, double percentageback);
	
	public boolean deleteCashBackCategory(int categoryId);
	
	public boolean updateCashBackCategory(int categoryId, int option, Object obj);
	
	public List<CategoryCashBack> getCashBackCategories(int cardId);

	void printCashBackCategories(int cardId);
}
