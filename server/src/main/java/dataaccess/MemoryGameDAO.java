package dataaccess;


import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private static final HashMap<Integer, GameData> games = new HashMap<>();
    private static int nextGameID = 1;

    @Override
    public int createGame(GameData game){
        int gameID = nextGameID++;
        GameData newGame = new GameData(gameID,game.whiteUsername(),game.blackUsername(),game.gameName(),game.game());
        games.put(gameID, newGame);
        return gameID;
    }

    @Override
    public GameData getGame(Integer gameID){
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> getGames() {
        return games.values();
    }

    @Override
    public void updateGame(GameData gamedata){
        games.put(gamedata.GameID(), gamedata);
    }

    @Override
    public void clearGames(){
        games.clear();
    }

}
