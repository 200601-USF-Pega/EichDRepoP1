package com.revature.creditcardrewardtracker.menu;

public class AdminMenuFactory {
	
	public IUserMenu getMenu(boolean isAdmin) {
		if (isAdmin == true) {
			return new AdminMenu();
		} else {
			return new MainMenu();
		}
	}

}
