package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest request) throws DataAccessException, SQLException {
        if(request.username() == null || request.username().isEmpty() || request.email() == null || request.email().isEmpty() || request.password() == null || request.password().isEmpty()){
            throw new DataAccessException("Error: bad request");
        }
        if(userDAO.getUser(request.username()) != null){
            throw new DataAccessException("Error: already taken");
        }

        UserData newUser = new UserData(request.username(), request.password(), request.email());
        userDAO.createUser(newUser);

        AuthData newToken = authDAO.createAuth(newUser.username());
        return new RegisterResult(newUser.username(), newToken.authToken());
    }

    public record RegisterRequest(String username, String password, String email) {
    }

    public record RegisterResult(String username, String authToken) {
    }

    public LoginResult login(LoginRequest request) throws DataAccessException, SQLException {
        if(request.username() == null || request.password() == null){
            throw new DataAccessException("Error: bad request");
        }

        UserData user = userDAO.getUser(request.username());

        if (user == null || !org.mindrot.jbcrypt.BCrypt.checkpw(request.password(), user.password())) {
            throw new DataAccessException("Error: unauthorized");
        }

        AuthData newToken = authDAO.createAuth(request.username());
        return new LoginResult(request.username(), newToken.authToken());
    }

    public record LoginRequest(String username, String password){
    }

    public record LoginResult(String username, String authToken){
    }

    public void logout(String authToken) throws DataAccessException, SQLException {
        AuthData authData = authDAO.getAuth(authToken);
        if(authData == null){
            throw new DataAccessException("Error: unauthorized"); //Missing field info
        }
        authDAO.deleteAuth(authToken);
    }

}