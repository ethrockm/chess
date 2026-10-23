package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameDAO {
    private static final HashMap<String, GameData> games = new HashMap<>();
    public static void createGame(int ID, String wUsername, String bUsername, String gameName, ChessGame chessGame){
        GameData game = new GameData(ID,wUsername,bUsername,gameName, chessGame);
        games.put(gameName, game);
    }

    public static GameData getGame(String gameName){
        return games.get(gameName);
    }

    public static List<String> listGame(){
        List<String> gameList = new ArrayList<>(games.keySet());
        return gameList;
    }

    void updateGame(int ID, String wUsername, String bUsername, String gameName, ChessGame chessGame){

    }

    public void clearGames(){

    }

}
