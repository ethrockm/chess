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
        //If not all fields are filled, then throw exception
        if(request.username() == null || request.username().isEmpty() || request.email() == null || request.email().isEmpty() || request.password() == null || request.password().isEmpty()){
            throw new DataAccessException("Error: bad request"); //Missing field info
        }
        //If username already taken, the throw exception
        if(userDAO.getUser(request.username()) != null){
            throw new DataAccessException("Error: already taken");
        }
        String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(request.password(), org.mindrot.jbcrypt.BCrypt.gensalt());
        // Create the User
        UserData newUser = new UserData(request.username(),hashedPassword,request.email());
        userDAO.createUser(newUser);
        //Get an AuthToken
        AuthData newToken = authDAO.createAuth(newUser.username());
        //Return the RegisterResult
        return new RegisterResult(newUser.username(),newToken.authToken());
    }

    public record RegisterRequest(String username, String password, String email) {
    }

    public record RegisterResult(String username, String authToken) {
    }

    public LoginResult login(LoginRequest request) throws DataAccessException, SQLException {
        if(request.username() == null || request.username().isEmpty() || request.password() == null || request.password().isEmpty()){
            throw new DataAccessException("Error: bad request"); //Missing field info
        }
        if(userDAO.getUser(request.username()) == null || !org.mindrot.jbcrypt.BCrypt.checkpw(userDAO.getUser(request.username()).password(), request.password())){
            throw new DataAccessException("Error: unauthorized"); //Wrong password
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