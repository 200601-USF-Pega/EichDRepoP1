package test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.revature.creditcardrewardtracker.models.CreditCardReward;

class CategoryCashBackTest {
	
	CreditCardReward categoryTest = new CreditCardReward();

	@Test
	void testSetAndGetCategory() {
		categoryTest.setCategoryOfCashBack("Disney");
		assertEquals("Disney", categoryTest.getCategoryOfCashBack());
	}
	
	@Test
	void testSetAndGetCashBackPercentage() {
		categoryTest.setPercentageOfCashBack(0.05);
		assertEquals(0.05, categoryTest.getPercentageOfCashBack(), 0.0001);
	}

}
