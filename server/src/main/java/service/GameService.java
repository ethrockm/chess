package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.List;

import static dataaccess.GameDAO.getGame;
import static dataaccess.UserDAO.getUser;


public class GameService {
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

//    public static List<String> listGame(){
//        List<String> gameList = new ArrayList<>(games.keySet());
//        return gameList;
//    }

    void updateGame(int ID, String wUsername, String bUsername, String gameName, ChessGame chessGame){

    }

    public CreateGameResult createGame(CreateGameRequest request, String authToken) throws DataAccessException {
        if(authDAO.getAuth(authToken) == null){
            throw new DataAccessException("Error: unauthorized"); //Missing field info
        }
        //If not all fields are filled, then throw exception
        if(request.gameName().isEmpty() || getGame(request.gameName()) != null){
            throw new DataAccessException("Error: bad request"); //Missing field info
        }
        // Create the User
        GameData newGame = new GameData(0,null,null,request.gameName(),new ChessGame());
        GameDAO.createGame(newGame);
        return new CreateGameResult(newGame.GameID());
    }

    public record CreateGameRequest(String gameName){}

    public record CreateGameResult(int ID){}

    void joinGame(){

    }
}
