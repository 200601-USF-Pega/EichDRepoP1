package com.revature.creditcardrewardtracker.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.creditcardrewardtracker.models.CategoryCashBack;
import com.revature.creditcardrewardtracker.web.ConnectionManager;

public class CreditCardRewardsRepoDB implements ICreditCardRewardsRepo {
	
	
	@Override
	public List<CategoryCashBack> getCashBackCategories(int cardId) {
		List<CategoryCashBack> categories = new ArrayList<CategoryCashBack>();
				
		try {
			Statement ns = ConnectionManager.getConnection().createStatement();
			ResultSet RSCats = ns.executeQuery("SELECT * FROM creditcardrewards"
					+ " WHERE cardid = " + cardId + ";");
			while (RSCats.next()) {
				CategoryCashBack tempCat = new CategoryCashBack();
				tempCat.setCategoryOfCashBack(RSCats.getString("category"));
				tempCat.setPercentageOfCashBack(RSCats.getDouble("percentageofcashback"));
				categories.add(tempCat);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return categories;
	}
	
	@Override
	public void printCashBackCategories(int cardId) {
		try {
			Statement ns = ConnectionManager.getConnection().createStatement();
			ResultSet rs = ns.executeQuery("SELECT * FROM creditcardrewards"
					+ " WHERE cardid = " + cardId + ";");
			ResultSetMetaData rsmd = rs.getMetaData();
			   int columnsNumber = rsmd.getColumnCount();
			   while (rs.next()) {
			       for (int i = 1; i <= columnsNumber; i++) {
			           if (i > 1) System.out.print(",  ");
			           String columnValue = rs.getString(i);
			           System.out.print(columnValue + " " + rsmd.getColumnName(i));
			       }
			       System.out.println("");
			   }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean addCashBackCategory(int cardId, String category, double percentageback) {
		try {
			String query = "INSERT INTO creditcardrewards(cardid, category, percentageofcashback) VALUES (?, ?, ?)";
			PreparedStatement categoryStatement = ConnectionManager.getConnection().prepareStatement(query);
			categoryStatement.setInt(1, cardId);
			categoryStatement.setString(2, category);
			categoryStatement.setDouble(3,  percentageback);
			categoryStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteCashBackCategory(int categoryId) {
		try {
			String query = "DELETE FROM creditcardrewards WHERE rewardid = ";
			Statement s = ConnectionManager.getConnection().createStatement();
			s.execute(query + categoryId + ";");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateCashBackCategory(int categoryId, int option, Object obj) {
		try {
			Statement s = ConnectionManager.getConnection().createStatement();
			switch (option) {
			case (0):
				// 0 - update category name
				s.executeUpdate("UPDATE creditcardrewards SET category = '" + (String) obj 
						+ "' WHERE rewardid = " + categoryId + ";");
				return true;
			case (1):
				// 1 - update rate
				s.executeUpdate("UPDATE creditcardrewards SET percentageofcashback = '" + (Double) obj 
						+ "' WHERE rewardid = " + categoryId + ";");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
