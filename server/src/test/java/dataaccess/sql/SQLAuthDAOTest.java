package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.SQLAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SQLAuthDAOTest {
    private SQLAuthDAO authDAO;

    @BeforeEach
    public void setUp() throws Exception {
        DatabaseManager.createDatabase();
        authDAO = new SQLAuthDAO();
        authDAO.clear();
    }

    @Test
    public void createAndGetAuthPositive() throws DataAccessException, SQLException {
        // 1. Create the auth and capture the object (which contains the random token)
        AuthData createdAuth = authDAO.createAuth("Elijah");

        // 2. Use the dynamically generated token to fetch it from the database
        AuthData retrieved = authDAO.getAuth(createdAuth.authToken());

        assertNotNull(retrieved);
        assertEquals("Elijah", retrieved.username());
    }

    @Test
    public void getAuthNegative() throws DataAccessException {
        AuthData retrieved = authDAO.getAuth("fakeToken");
        assertNull(retrieved);
    }

    @Test
    public void deleteAuthPositive() throws DataAccessException, SQLException {
        // 1. Create the auth and capture the token
        AuthData createdAuth = authDAO.createAuth("Elijah");

        // 2. Delete using the generated token
        authDAO.deleteAuth(createdAuth.authToken());

        // 3. Try to fetch it again, it should be null
        AuthData retrieved = authDAO.getAuth(createdAuth.authToken());
        assertNull(retrieved);
    }
}