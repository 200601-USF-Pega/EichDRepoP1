package test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.revature.creditcardrewardtracker.service.ValidationService;

class ValidationServiceTest {
	
	ValidationService validation = new ValidationService(null);

	@Test
	void testUsernameLengthValidation() {
		assertTrue(validation.usernameLengthValidation("user2134"));
		assertFalse(validation.usernameLengthValidation("ab"));
		assertFalse(validation.usernameLengthValidation("ThisIsAReallyReallyReallyLongUsernamePhewImWindedJustTypingItImagineTypingThisEverySingleTime"));
	}

	@Test
	void testPasswordLengthValidation() {
		assertTrue(validation.passwordLengthValidation("password"));
		assertFalse(validation.passwordLengthValidation("bob"));
		assertFalse(validation.passwordLengthValidation("ThisIsAReallyReallyReallyLongPasswordPhewImWindedJustTypingItImagineTypingThisEverySingleTime"));
	}

}
