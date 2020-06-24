package com.revature.creditcardrewardtracker.models;

public class CreditCardReward {
	
	private String categoryOfCashBack;
	private double percentageOfCashBack;
	
	public CreditCardReward() {
		
	}
	
	public CreditCardReward(String categoryOfCashBack, double percentageOfCashBack) {
		this.categoryOfCashBack = categoryOfCashBack;
		this.percentageOfCashBack = percentageOfCashBack;
	}

	public String getCategoryOfCashBack() {
		return categoryOfCashBack;
	}

	public void setCategoryOfCashBack(String categoryOfCashBack) {
		this.categoryOfCashBack = categoryOfCashBack;
	}

	public double getPercentageOfCashBack() {
		return percentageOfCashBack;
	}

	public void setPercentageOfCashBack(double percentageOfCashBack) {
		this.percentageOfCashBack = percentageOfCashBack;
	}

	@Override
	public String toString() {
		return '\n' + "Category = " + categoryOfCashBack + ", Cash Back Rate = "
				+ percentageOfCashBack;
	}

}
