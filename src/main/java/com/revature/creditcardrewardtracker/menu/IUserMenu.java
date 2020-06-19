package com.revature.creditcardrewardtracker.menu;

import java.sql.Connection;
import java.util.Scanner;

public interface IUserMenu {

	public void menu(Scanner sc, String username, Connection connection);
}
