package service;
import dataaccess.*;
import model.GameData;
import model.UserData;
import chess.ChessGame;
import org.junit.jupiter.api.*;

public class ClearServiceTest {
    private MemoryUserDAO userDAO;
    private MemoryAuthDAO authDAO;
    private MemoryGameDAO gameDAO;
    private ClearService clearService;

    @BeforeEach
    public void setup() throws DataAccessException {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        clearService = new ClearService(userDAO, gameDAO, authDAO);

        // Inject dummy data to verify the clear function actually removes it
        userDAO.createUser(new UserData("testUser", "password", "email@test.com"));
        authDAO.createAuth("testUser");
        gameDAO.createGame(new GameData(0, null, null, "testGame", new ChessGame()));
    }

    @Test
    public void clearSuccess() throws DataAccessException {
        clearService.clear();

        // Assert all databases are completely empty
        Assertions.assertNull(userDAO.getUser("testUser"), "UserDAO should be empty");
        Assertions.assertEquals(0, gameDAO.getGames().size(), "GameDAO should be empty");
        // Note: You may need a size() or similar method in AuthDAO if getAuth("dummyToken") isn't easily testable without the token string
    }
}

