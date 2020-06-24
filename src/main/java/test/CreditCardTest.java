package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import com.revature.creditcardrewardtracker.models.CreditCardReward;
import com.revature.creditcardrewardtracker.models.CreditCard;

class CreditCardTest {
	
	CreditCard testCard = new CreditCard();
	CreditCardReward testCategory = new CreditCardReward();
	List<CreditCardReward> list = new ArrayList<CreditCardReward>();


	@Test
	void testSetandGetCreditCardName() {
		testCard.setCreditCardName("Disney");
		assertEquals("Disney", testCard.getCreditCardName());
	}

	@Test
	void testSetandGetCreditCardID() {
		testCard.setCreditCardID(15);
		assertSame(15, testCard.getCreditCardID());
	}


	@Test
	void testSetandGetCardCashBackCategories() {
		testCategory.setCategoryOfCashBack("Disney");
		testCategory.setPercentageOfCashBack(0.01);
		list.add(testCategory);
		testCard.setCardCashBackCategories(list);
		assertEquals(list.get(0), testCard.getCardCashBackCategories().get(0));
	}


}
