package com.revature.creditcardrewardtracker.service;

import java.util.Scanner;

//input validation adapted from the amazing Marielle Nolasco's Tour of Heroes Application from
//https://github.com/200601-USF-Pega/trainer-code/blob/master/Week1Java/TourOfHeros/TourOfHeros/src/com/revature/tourofheroes/service/ValidationService.java

public class InputValidationService {
	
	Scanner sc;

	public InputValidationService(Scanner sc) {
		this.sc = sc;
	}
	
	public String getValidStringInput() {
		String userInput = null;
		boolean invalid = true;
		while (invalid == true) {
			userInput = sc.nextLine();
			if(!userInput.isEmpty()) {
				invalid = false;
				break;
			} else {
				System.out.println("Please input non empty string");
			}
		}
		return userInput;
	}
	
	public int getValidInt() {
		boolean invalid = true;
		int userInput = 0;
		do {
			try {
				userInput = Integer.parseInt(sc.nextLine());
				invalid = false;
				break;
			} catch (NumberFormatException ex) {
				System.out.println("Please input valid integers");
				invalid = true;
			}
		} while(invalid==true);
		return userInput;
	}
	
	public double getValidDouble() {
		boolean invalid = true;
		double userInput = 0;
		do {
			try {
				userInput = Double.parseDouble(sc.nextLine());
				invalid = false;
				break;
			} catch (NumberFormatException ex) {
				System.out.println("Please input a valid double (i.e. 0.05)");
				invalid = true;
			}
		} while(invalid==true);
		return userInput;
	}
	
	public int getValidDate() {
		int date = 19000101;
	
		boolean invalid = true;
		do {
			try {
				date = Integer.parseInt(sc.nextLine());
				if (date >= 19000101 && date <= 21001230) {
					invalid = false;
					return date;
				} else {
					System.out.println("Please enter a date between 19000101 and 21001230.");
					invalid = true;
				}
			} catch (NumberFormatException ex) {
				System.out.println("Please input valid integers");
				invalid = true;
			}
		} while(invalid==true);
		
		return date;
	}
}
