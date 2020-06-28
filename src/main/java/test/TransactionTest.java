package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.sql.Date;

import org.junit.jupiter.api.Test;

import com.revature.creditcardrewardtracker.models.Transaction;

class TransactionTest {
	
	Transaction testTransaction = new Transaction();
	
	@Test
	void testSetAndGetCardId() {
		testTransaction.setCardID(1234);
		assertEquals(1234, testTransaction.getCardID());
	}
	
	@Test
	void testSetAndGetCategory() {
		testTransaction.setCategory("Dining");
		assertEquals("Dining", testTransaction.getCategory());
	}
	
	@Test
	void testSetAndGetTotal() {
		testTransaction.setTotal(231.1);
		assertEquals(231.1, testTransaction.getTotal(), 0.0001);
	}
	
	@Test
	void testSetAndGetCashbackTotal() {
		testTransaction.setCashBackTotal(5.21);
		assertEquals(5.21, testTransaction.getCashBackTotal(), 0.0001);
	}

}
