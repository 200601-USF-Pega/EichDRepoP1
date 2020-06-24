import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import com.revature.creditcardrewardtracker.menu.LogInMenu;
import com.revature.creditcardrewardtracker.web.ConnectionManager;

public class Driver {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		ConnectionManager connect = new ConnectionManager();
		Connection newConnection = connect.start();
		LogInMenu menu = new LogInMenu();
		menu.menu(sc, newConnection);
		
		sc.close();
		try {
			newConnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}

}
