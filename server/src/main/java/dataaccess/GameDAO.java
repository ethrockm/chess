package dataaccess;


import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class GameDAO {
    private static final HashMap<Integer, GameData> games = new HashMap<>();
    private static int nextGameID = 1;

    public static int createGame(GameData game){
        int gameID = nextGameID++;
        GameData newGame = new GameData(gameID,game.whiteUsername(),game.blackUsername(),game.gameName(),game.game());
        games.put(gameID, newGame);
        return gameID;
    }

    public static GameData getGame(Integer gameID){
        return games.get(gameID);
    }

    public static Collection<GameData> getGames() {
        return games.values();
    }

    public static void updateGame(GameData gamedata){
        games.put(gamedata.GameID(), gamedata);
    }

    public void clearGames(){
        games.clear();
    }

}
