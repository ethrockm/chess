package service;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import dataaccess.AuthDAO;

public class UserService {
    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public RegisterResult register(RegisterRequest request) throws DataAccessException {
        //If not all fields are filled, then throw exception
        if(request.username() == null || request.username().isEmpty() || request.email() == null || request.email().isEmpty() || request.password() == null || request.password().isEmpty()){
            throw new DataAccessException("Error: bad request"); //Missing field info
        }
        //If username already taken, the throw exception
        if(UserDAO.getUser(request.username()) != null){
            throw new DataAccessException("Error: already taken");
        }
        // Create the User
        UserData newUser = new UserData(request.username(),request.password(),request.email());
        userDAO.createUser(newUser);
        //Get an AuthToken
        AuthData authTokens = AuthDAO.createAuth(newUser.username());
        //Return the RegisterResult
        return new RegisterResult(newUser.username(),authTokens.authToken());
    }

    public record RegisterRequest(String username, String password, String email) {
    }

    public record RegisterResult(String username, String authToken) {
    }
}