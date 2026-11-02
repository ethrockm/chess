package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    int createGame(GameData game);

    GameData getGame(Integer gameID);

    Collection<GameData> getGames();

    void updateGame(GameData gamedata);

    void clearGames();
}
