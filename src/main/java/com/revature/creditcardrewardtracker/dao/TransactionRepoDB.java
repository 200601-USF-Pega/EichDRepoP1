package com.revature.creditcardrewardtracker.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.creditcardrewardtracker.models.Transaction;
import com.revature.creditcardrewardtracker.web.ConnectionManager;

//Java Util to SQL method adapted from Javin Paul's tutorial found on
//https://www.java67.com/2014/02/how-to-convert-javautildate-to-javasqldate-example.html#:~:text=Since%20both%20Date%20classes%20are,sql.

//Code for Printing the ResultSet was adapted from code written by lukas on Coderwall.com
//https://coderwall.com/p/609ppa/printing-the-result-of-resultset

public class TransactionRepoDB implements ITransactionRepo {
	
	@Override
	public void addTransaction(Transaction newTransaction) {
		try {
			PreparedStatement s = ConnectionManager.getConnection().prepareStatement("INSERT INTO "
					+ "transactionrecords(date, category, transactiontotal, "
					+ "cashbacktotal, cardid) VALUES (?, ?, ?, ?, ?)");
						
			s.setDate(1, convertLocalToSQLDate(newTransaction.getLDate()));
			s.setString(2, newTransaction.getCategory());
			s.setDouble(3,  newTransaction.getTotal());
			s.setDouble(4,  newTransaction.getCashBackTotal());
			s.setInt(5,  newTransaction.getCardID());
			s.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}  
	
	public Transaction getTransaction(int transactionId) {
		List<Transaction> transactionList = new ArrayList<Transaction>();
		
		try {
			Statement s = ConnectionManager.getConnection().createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM transactionrecords "
				+ "WHERE transactionid = " + transactionId + ";");
			
			transactionList = createTransactionList(rs, transactionList);
			return transactionList.get(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public List<Transaction> listTransactions(String username) {
		List<Transaction> transactionList = new ArrayList<Transaction>();
		
		try {
			Statement s = ConnectionManager.getConnection().createStatement();
			ResultSet rs;
			rs = s.executeQuery("SELECT transactionid, date, category, transactiontotal, cashbacktotal, t.cardid FROM transactionrecords as t "
				+ "INNER JOIN ("
					+	"SELECT cardid FROM creditcards "
					+	"WHERE username = '" + username
					+ 	"') AS c ON t.cardid = c.cardid;");
			
			//printResultSet(rs);

			transactionList = createTransactionList(rs, transactionList);
			
			return transactionList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public List<Transaction> listTransactionsForCategory(String username, String category) {
		List<Transaction> transactionList = new ArrayList<Transaction>();
		try {
			Statement s = ConnectionManager.getConnection().createStatement();
			ResultSet rs;
			rs = s.executeQuery("SELECT transactionid, date, category, transactiontotal, cashbacktotal, t.cardid FROM transactionrecords as t "
				+ "INNER JOIN ("
					+	"SELECT cardid FROM creditcards "
					+	"WHERE username = '" + username
					+ 	"') AS c ON t.cardid = c.cardid "
					+	"WHERE category = '" + category + "';");
			
			//ResultSet copy = rs;
			//printResultSet(copy);

			transactionList = createTransactionList(rs, transactionList);
						
			return transactionList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return null;
	}

	@Override
	public List<Transaction> listTransactionsForCreditCard(String username, int cardID) {
		List<Transaction> transactionList = new ArrayList<Transaction>();
		try {
			Statement s = ConnectionManager.getConnection().createStatement();
			ResultSet rs;
			rs = s.executeQuery("SELECT transactionid, date, category, transactiontotal, cashbacktotal, t.cardid FROM transactionrecords as t "
				+ "INNER JOIN ("
					+	"SELECT cardid FROM creditcards "
					+	"WHERE username = '" + username
					+ 	"') AS c ON t.cardid = c.cardid "
					+	"WHERE c.cardId = " + cardID + ";");
			
			transactionList = createTransactionList(rs, transactionList);
			
			//printResultSet(rs);
			
			return transactionList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return null;
	}

	@Override
	public List<Transaction> listTransactionsForDateRange(String username, java.time.LocalDate startDate, java.time.LocalDate endDate) {
		List<Transaction> transactionList = new ArrayList<Transaction>();
		java.sql.Date sqlStartDate = convertLocalToSQLDate(startDate);
		java.sql.Date sqlEndDate = convertLocalToSQLDate(endDate);
		try {
			Statement s = ConnectionManager.getConnection().createStatement();
			ResultSet rs;
			rs = s.executeQuery("SELECT transactionid, date, category, transactiontotal, cashbacktotal, t.cardid FROM transactionrecords as t "
				+ "INNER JOIN ("
					+	"SELECT cardid FROM creditcards "
					+	"WHERE username = '" + username
					+ 	"') AS c ON t.cardid = c.cardid "
					+	"WHERE date BETWEEN '" + sqlStartDate + "' AND '" + sqlEndDate + "';");
			
			transactionList = createTransactionList(rs, transactionList);	
			return transactionList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void updateTransaction(int transactionId, int option, Object obj) {
		try {
			Statement s = ConnectionManager.getConnection().createStatement();
				switch (option) {
					case (1) :
						//date
						java.time.LocalDate date = (java.time.LocalDate) obj;
						java.sql.Date sqlDate = convertLocalToSQLDate(date);
						s.executeUpdate("UPDATE transactionrecords AS t " +
								"SET date = '" + sqlDate + "' WHERE t.transactionid = " + transactionId + ";");	
						System.out.println("Date update executed.");
						break;
					case (2) :
						//category
						String category = (String) obj;
						s.executeUpdate("UPDATE transactionrecords AS t " +
							"SET category = '" + category + "' WHERE t.transactionid = " + transactionId + ";");
						System.out.println("Category update executed.");
						break;
					case (3) :
						//transaction total
						double total = (double) obj;
						s.executeUpdate("UPDATE transactionrecords AS t " +
							"SET transactiontotal = " + total + " WHERE t.transactionid = " + transactionId + ";");
						System.out.println("Transaction Total update executed.");
						break;
					case (4) :
						//card
						int cardID = (int) obj;
						s.executeUpdate("UPDATE transactionrecords AS t " +
							"SET cardid = " + cardID + " WHERE t.transactionid = " + transactionId + ";");
						System.out.println("Card ID update executed.");
						break;
					default :
						System.out.println("No update executed.");
				}		
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean deleteTransaction(int id) {
		
		try {
			Statement s = ConnectionManager.getConnection().createStatement();
			s.execute("DELETE FROM transactionrecords "
				+ "WHERE transactionid = " + id +";");
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	private static java.sql.Date convertLocalToSQLDate(java.time.LocalDate date) {
		java.sql.Date sqlDate = Date.valueOf(date);
		return sqlDate;
	}
	
	private static java.time.LocalDate convertSQLtoLocalDate(java.sql.Date date) {
		java.time.LocalDate localDate = date.toLocalDate();
		return localDate;
	}
	
	private static List<Transaction> createTransactionList(ResultSet rs, List<Transaction> transactionList) {
		try {
			while(rs.next()) {
				Transaction tempTransaction = new Transaction();
				tempTransaction.setCardID(rs.getInt("cardid"));
				tempTransaction.setCategory(rs.getString("category"));
				tempTransaction.setTotal(rs.getDouble("transactiontotal"));
				tempTransaction.setDate(convertSQLtoLocalDate(rs.getDate("date")));
				tempTransaction.setCashBackTotal(rs.getDouble("cashbacktotal"));
				tempTransaction.setTransactionId(rs.getInt("transactionid"));
				transactionList.add(tempTransaction);
			}
			return transactionList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void printResultSet(String username) {
		try {
			Statement s = ConnectionManager.getConnection().createStatement();
			ResultSet rs;
			rs = s.executeQuery("SELECT * FROM transactionrecords as t "
				+ "INNER JOIN ("
					+	"SELECT cardid FROM creditcards "
					+	"WHERE username = '" + username
					+ 	"') AS c ON t.cardid = c.cardid;");
			ResultSetMetaData rsmd = rs.getMetaData();
			   System.out.println("Printing out transactions");
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

	
}
