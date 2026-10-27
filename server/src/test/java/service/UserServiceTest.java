package service;

import dataaccess.*;
import org.junit.jupiter.api.*;

public class UserServiceTest {

    private UserDAO userDAO;
    private AuthDAO authDAO;
    private UserService userService;

    @BeforeEach
    public void setup() throws DataAccessException {
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        userService = new UserService(userDAO, authDAO);
        userDAO.clearUsers();
        authDAO.clear();
    }

    @Test
    public void registerSuccess() throws DataAccessException {
        UserService.RegisterRequest request = new UserService.RegisterRequest("newUser", "pass", "email@test.com");
        UserService.RegisterResult result = userService.register(request);

        Assertions.assertNotNull(result.authToken());
        Assertions.assertEquals("newUser", result.username());
    }

    @Test
    public void registerFailDuplicate() throws DataAccessException {
        UserService.RegisterRequest request = new UserService.RegisterRequest("duplicateUser", "pass", "email");
        userService.register(request);

        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            userService.register(request);
        });
        Assertions.assertEquals("Error: already taken", exception.getMessage());
    }

    @Test
    public void loginSuccess() throws DataAccessException {
        userService.register(new UserService.RegisterRequest("loginUser", "pass", "email"));
        UserService.LoginRequest loginReq = new UserService.LoginRequest("loginUser", "pass");

        UserService.LoginResult result = userService.login(loginReq);
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    public void loginFailWrongPassword() throws DataAccessException {
        userService.register(new UserService.RegisterRequest("loginUser", "pass", "email"));
        UserService.LoginRequest loginReq = new UserService.LoginRequest("loginUser", "WRONG");

        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            userService.login(loginReq);
        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    public void logoutSuccess() throws DataAccessException {
        UserService.RegisterResult regResult = userService.register(new UserService.RegisterRequest("logoutUser", "pass", "email"));

        // Logout should succeed without throwing an exception
        Assertions.assertDoesNotThrow(() -> {
            userService.logout(regResult.authToken());
        });

        // Token should be removed from database
        Assertions.assertNull(authDAO.getAuth(regResult.authToken()));
    }

    @Test
    public void logoutFailBadToken() {
        DataAccessException exception = Assertions.assertThrows(DataAccessException.class, () -> {
            userService.logout("invalid-token-123");
        });
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }
}
