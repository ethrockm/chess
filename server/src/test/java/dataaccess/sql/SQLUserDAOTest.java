package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.SQLUserDAO;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

public class SQLUserDAOTest {
    private SQLUserDAO userDAO;

    @BeforeEach
    public void setUp() throws Exception {
        // Initialize tables if they don't exist
        DatabaseManager.createDatabase();

        userDAO = new SQLUserDAO();
        // Wipe the users table clean before each test so we have a fresh start
        userDAO.clearUsers();
    }

    @Test
    public void createUserPositive() throws DataAccessException {
        UserData user = new UserData("Elijah", "secure123", "elijah@test.com");

        // Insert into MySQL
        userDAO.createUser(user);

        // Retrieve from MySQL
        UserData retrievedUser = userDAO.getUser("Elijah");

        assertNotNull(retrievedUser);
        assertEquals("Elijah", retrievedUser.username());
        assertEquals("elijah@test.com", retrievedUser.email());

        // Verify the password was hashed and is no longer plain-text
        assertNotEquals("secure123", retrievedUser.password());
        assertTrue(BCrypt.checkpw("secure123", retrievedUser.password()));
    }

    @Test
    public void getUserNegative() throws DataAccessException {
        // Attempt to fetch a user that doesn't exist
        UserData retrievedUser = userDAO.getUser("GhostUser");
        assertNull(retrievedUser);
    }
}