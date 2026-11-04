package dataaccess;

import model.GameData;

import java.sql.SQLException;
import java.util.Collection;

public interface GameDAO {
    int createGame(GameData game) throws SQLException, DataAccessException;

    GameData getGame(Integer gameID) throws DataAccessException;

    Collection<GameData> getGames();

    void updateGame(GameData gamedata) throws SQLException, DataAccessException;

    void clearGames() throws SQLException, DataAccessException;
}
