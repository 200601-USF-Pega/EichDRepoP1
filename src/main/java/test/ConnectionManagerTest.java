package test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;

import org.junit.jupiter.api.Test;

import com.revature.creditcardrewardtracker.dao.ConnectionManager;

class ConnectionManagerTest {

	@Test
	void testConnectionManagerCreated() {
		ConnectionManager manager = new ConnectionManager();
		Connection connection = manager.start();
		assertNotNull(connection);
	}

}
