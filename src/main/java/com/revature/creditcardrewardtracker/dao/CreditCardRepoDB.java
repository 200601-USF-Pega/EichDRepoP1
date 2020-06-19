package com.revature.creditcardrewardtracker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.creditcardrewardtracker.models.CategoryCashBack;
import com.revature.creditcardrewardtracker.models.CreditCard;

public class CreditCardRepoDB implements ICreditCardRepo {
	
	Connection connection;
	
	public CreditCardRepoDB(Connection connection) {
		this.connection = connection;
	}

	@Override
	public CreditCard addCreditCard(String username, CreditCard card) {
		//1. insert credit card to creditcards table
		//2. add creditcardcategories to creditcardrewards table
	
		try {
			String query1 = "INSERT INTO creditcards (cardname, username) VALUES (?, ?)";
			PreparedStatement creditCardStatement = connection.prepareStatement(query1, PreparedStatement.RETURN_GENERATED_KEYS);
			creditCardStatement.setString(1,  card.getCreditCardName());
			creditCardStatement.setString(2,  username);
			creditCardStatement.executeUpdate();
			
			ResultSet rs = creditCardStatement.getGeneratedKeys();
			int cardid = rs.next() ? rs.getInt(1) : 0;
			card.setCreditCardID(cardid);
			
			ICreditCardRewardsRepo ccrr = new CreditCardRewardsRepoDB(connection);
			for (CategoryCashBack category : card.getCardCashBackCategories()) {
				ccrr.addCashBackCategory(cardid, category.getCategoryOfCashBack(), category.getPercentageOfCashBack());
			}
			
			return card;
		} catch (SQLException e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<CreditCard> getCreditCards(String username) {
		List<CreditCard> listOfCards = new ArrayList<CreditCard>();
		ResultSet RSCards;
		ResultSet RSCats;
		try {
			Statement s = connection.createStatement();
			RSCards = s.executeQuery("SELECT * FROM CreditCards"
					+ " WHERE username = '" + username + "';");

			//ResultSet rsa = s.getResultSet();
			while (RSCards.next()) {
				Statement ns = connection.createStatement();
				CreditCard tempCard = new CreditCard();
				tempCard.setCreditCardID(RSCards.getInt("cardid"));
				tempCard.setCreditCardName(RSCards.getString("cardname"));
				
				List<CategoryCashBack> categories = new ArrayList<CategoryCashBack>();
				RSCats = ns.executeQuery("SELECT * FROM creditcardrewards"
						+ " WHERE cardid = " + tempCard.getCreditCardID() + ";");
				//ResultSet rsn = s.getResultSet();
				while (RSCats.next()) {
					CategoryCashBack tempCat = new CategoryCashBack();
					tempCat.setCategoryOfCashBack(RSCats.getString("category"));
					tempCat.setPercentageOfCashBack(RSCats.getDouble("percentageofcashback"));
					categories.add(tempCat);
				}
				
				tempCard.setCardCashBackCategories(categories);
				
				listOfCards.add(tempCard);
			}
			
			return listOfCards;
		} catch (SQLException e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public boolean deleteCard(int cardId) {
		try {
			Statement s = connection.createStatement();
			s.execute("DELETE FROM creditcards WHERE cardid = '" + cardId + "';");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("Card not found. Please try again.");
		return false;
	}
	
	@Override
	public boolean updateCard(int cardId, String name) {
		try {	
			Statement s = connection.createStatement();
			s.executeUpdate("UPDATE creditcards AS c " +
					"SET cardname = '" + name + "' WHERE c.cardId = " + cardId + ";");
			return true;
		} catch (SQLException e) {
			e.getMessage();
			e.printStackTrace();
		}
		return false;
	}


}
