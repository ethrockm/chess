package dataaccess;


import model.GameData;
import java.util.HashMap;

public class GameDAO {
    private static final HashMap<String, GameData> games = new HashMap<>();

    public static void createGame(GameData game){
        games.put(game.gameName(), game);
    }

    public static GameData getGame(String gameName){
        return games.get(gameName);
    }

    public void clearGames(){
        games.clear();
    }

}
