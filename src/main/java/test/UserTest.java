package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.revature.creditcardrewardtracker.models.CreditCard;
import com.revature.creditcardrewardtracker.models.User;

class UserTest {
	
	User testUser = new User();

	@Test
	void testSetAndGetUsername() {
		testUser.setUsername("Danny");
		assertEquals("Danny", testUser.getUsername());
	}
	
	@Test
	void testSetAndGetPassword() {
		testUser.setPassword("StrongPassword");
		assertEquals("StrongPassword", testUser.getPassword());
	}
	
	@Test
	void testNormalUserForAdminRights() {
		assertFalse(testUser.isAdmin());
	}
	
	@Test
	void testAdminUserForAdminRights() {
		testUser.setAdmin(true);
		assertTrue(testUser.isAdmin());
	}
	
	@Test
	void testAddAndGetCardsOnFile() {
		CreditCard testCard = new CreditCard();
		testUser.addCardsToFile(testCard);
		assertEquals(testCard, testUser.getCardsOnFile().get(0));
	}

}
