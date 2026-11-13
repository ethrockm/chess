package client;

import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        // Initialize your ServerFacade with the dynamic test server port
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clearDatabase() throws Exception {
        // Clears the database before every test for full isolation
        facade.clear();
    }

    @Test
    public void registerPositive() throws Exception {
        AuthData auth = facade.register("player1", "password123", "p1@email.com");
        assertNotNull(auth);
        assertNotNull(auth.authToken());
        assertEquals("player1", auth.username());
    }

    @Test
    public void registerNegative() {
        // Trying to register a duplicate username should fail
        assertThrows(Exception.class, () -> {
            facade.register("player1", "password123", "p1@email.com");
            facade.register("player1", "differentPass", "p2@email.com");
        });
    }

    @Test
    public void loginPositive() throws Exception {
        facade.register("loginUser", "securePass", "login@email.com");
        AuthData auth = facade.login("loginUser", "securePass");
        assertNotNull(auth);
        assertNotNull(auth.authToken());
        assertEquals("loginUser", auth.username());
    }

    @Test
    public void loginNegative() {
        // Logging in with non-existent credentials should fail
        assertThrows(Exception.class, () -> {
            facade.login("fakeUser", "wrongPass");
        });
    }

    @Test
    public void logoutPositive() throws Exception {
        AuthData auth = facade.register("logoutUser", "pass", "logout@email.com");
        assertDoesNotThrow(() -> facade.logout(auth.authToken()));
    }

    @Test
    public void logoutNegative() {
        // Logging out with an invalid token should fail
        assertThrows(Exception.class, () -> {
            facade.logout("bogus-token-123");
        });
    }

    @Test
    public void createGamePositive() throws Exception {
        AuthData auth = facade.register("creator", "pass", "creator@email.com");
        int gameID = facade.createGame("Championship", auth.authToken());
        assertTrue(gameID > 0);
    }

    @Test
    public void createGameNegative() {
        // Creating a game with an invalid token should fail
        assertThrows(Exception.class, () -> {
            facade.createGame("Unauthorized Game", "fake-token");
        });
    }

    @Test
    public void listGamePositive() throws Exception {
        AuthData auth = facade.register("lister", "pass", "lister@email.com");
        facade.createGame("Game One", auth.authToken());
        facade.createGame("Game Two", auth.authToken());

        var games = facade.listGame(auth.authToken());
        assertNotNull(games);
        assertEquals(2, games.size());
    }

    @Test
    public void joinGamePositive() throws Exception {
        AuthData auth = facade.register("joiner", "pass", "joiner@email.com");
        int gameID = facade.createGame("Open Table", auth.authToken());

        assertDoesNotThrow(() -> facade.joinGame("WHITE", gameID, auth.authToken()));
    }

    @Test
    public void joinGameNegative() {
        // Joining a non-existent game or using a bad token should fail
        assertThrows(Exception.class, () -> {
            facade.joinGame("BLACK", 99999, "fake-token");
        });
    }
}