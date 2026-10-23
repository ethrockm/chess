package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.DataAccessException;

public class ClearService {
    private static UserDAO userDAO;
    private static GameDAO gameDAO;
    private static AuthDAO authDAO;

    public ClearService (UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public static void clear() throws DataAccessException{
        userDAO.clearUsers();
        authDAO.clear();
        gameDAO.clearGames();
    }
}
