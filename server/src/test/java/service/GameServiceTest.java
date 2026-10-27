package service;

import dataaccess.*;
import org.junit.jupiter.api.*;

public class GameServiceTest {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private GameService gameService;
    private String validAuthToken;

    @BeforeEach
    public void setup() throws DataAccessException {
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        gameDAO = new GameDAO();
        gameService = new GameService(gameDAO, authDAO);

        userDAO.clearUsers();
        authDAO.clear();
        gameDAO.clearGames();

        // Register a dummy user to get a valid token for the game tests
        UserService userService = new UserService(userDAO, authDAO);
        UserService.RegisterResult regResult = userService.register(new UserService.RegisterRequest("player1", "pass", "email"));
        validAuthToken = regResult.authToken();
    }

    @Test
    public void createGameSuccess() throws DataAccessException {
        GameService.CreateGameRequest request = new GameService.CreateGameRequest("My Chess Game");
        GameService.CreateGameResult result = gameService.createGame(request, validAuthToken);

        Assertions.assertTrue(result.gameID() >= 0);
        Assertions.assertNotNull(gameDAO.getGame(result.gameID()));
    }

    @Test
    public void createGameFailUnauthorized() {
        GameService.CreateGameRequest request = new GameService.CreateGameRequest("Bad Auth Game");

        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.createGame(request, "fake-token");
        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    public void listGamesSuccess() throws DataAccessException {
        gameService.createGame(new GameService.CreateGameRequest("Game 1"), validAuthToken);
        gameService.createGame(new GameService.CreateGameRequest("Game 2"), validAuthToken);

        GameService.ListGameResult result = gameService.listGame(validAuthToken);
        Assertions.assertEquals(2, result.games().size());
    }

    @Test
    public void listGamesFailUnauthorized() {
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.listGame("fake-token");
        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    public void joinGameSuccess() throws DataAccessException {
        GameService.CreateGameResult createResult = gameService.createGame(new GameService.CreateGameRequest("Join Game Test"), validAuthToken);

        GameService.JoinGameRequest joinReq = new GameService.JoinGameRequest("WHITE", createResult.gameID());
        gameService.joinGame(joinReq, validAuthToken);

        // Verify the database actually updated the username
        Assertions.assertEquals("player1", gameDAO.getGame(createResult.gameID()).whiteUsername());
    }

    @Test
    public void joinGameFailColorTaken() throws DataAccessException {
        // Create game and join as WHITE
        GameService.CreateGameResult createResult = gameService.createGame(new GameService.CreateGameRequest("Color Taken Game"), validAuthToken);
        gameService.joinGame(new GameService.JoinGameRequest("WHITE", createResult.gameID()), validAuthToken);

        // Try to join as WHITE again
        GameService.JoinGameRequest badJoinReq = new GameService.JoinGameRequest("WHITE", createResult.gameID());

        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.joinGame(badJoinReq, validAuthToken);
        });
        Assertions.assertEquals("Error: already taken", exception.getMessage());
    }
}
