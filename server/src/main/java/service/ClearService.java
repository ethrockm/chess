package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.DataAccessException;

public class ClearService {
    private static MemoryUserDAO userDAO;
    private static MemoryGameDAO gameDAO;
    private static MemoryAuthDAO authDAO;

    public ClearService (MemoryUserDAO userDAO, MemoryGameDAO gameDAO, MemoryAuthDAO authDAO){
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
