package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public interface AuthDAO {

    public AuthData createAuth(String username) throws SQLException, DataAccessException;

    public AuthData getAuth(String username) throws DataAccessException;

    public void deleteAuth(String username) throws SQLException, DataAccessException;

    public void clear() throws SQLException, DataAccessException;

}
