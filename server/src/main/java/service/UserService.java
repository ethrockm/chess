package service;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import dataaccess.AuthDAO;

import java.util.Objects;

import static dataaccess.UserDAO.getUser;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest request) throws DataAccessException {
        //If not all fields are filled, then throw exception
        if(request.username() == null || request.username().isEmpty() || request.email() == null || request.email().isEmpty() || request.password() == null || request.password().isEmpty()){
            throw new DataAccessException("Error: bad request"); //Missing field info
        }
        //If username already taken, the throw exception
        if(getUser(request.username()) != null){
            throw new DataAccessException("Error: already taken");
        }
        // Create the User
        UserData newUser = new UserData(request.username(),request.password(),request.email());
        userDAO.createUser(newUser);
        //Get an AuthToken
        AuthData newToken = AuthDAO.createAuth(newUser.username());
        //Return the RegisterResult
        return new RegisterResult(newUser.username(),newToken.authToken());
    }

    public record RegisterRequest(String username, String password, String email) {
    }

    public record RegisterResult(String username, String authToken) {
    }

    public LoginResult login(LoginRequest request) throws DataAccessException {
        if(request.username() == null || request.username().isEmpty() || request.password() == null || request.password().isEmpty()){
            throw new DataAccessException("Error: bad request"); //Missing field info
        }
        if(getUser(request.username) == null || !Objects.equals(getUser(request.username()).password(), request.password())){
            throw new DataAccessException("Error: unauthorized"); //Wrong password
        }

        AuthData newToken = AuthDAO.createAuth(request.username());
        return new LoginResult(request.username(), newToken.authToken());
    }

    public record LoginRequest(String username, String password){
    }

    public record LoginResult(String username, String authToken){
    }

    public void logout(String authToken) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if(authData == null){
            throw new DataAccessException("Error: unauthorized"); //Missing field info
        }
        AuthDAO.deleteAuth(authToken);
    }

}