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

    CreateGameResult createGame(CreateGameRequest request, String authToken) throws DataAccessException {
        //If not all fields are filled, then throw exception
        if(request.ID() == 0 || request.wUsername().isEmpty() || request.bUsername().isEmpty() || request.gameName().isEmpty() || request.chessGame() == null){
            throw new DataAccessException("Error: bad request"); //Missing field info
        }
        //If username already taken, the throw exception
        AuthData authData = authDAO.getAuth(authToken);
        if(authData == null){
            throw new DataAccessException("Error: unauthorized"); //Missing field info
        }
        // Create the User
        GameData newGame = new GameData(request.ID(),request.wUsername(),request.bUsername(),request.gameName(),request.chessGame());
        GameDAO.createGame(newGame);
        return new CreateGameResult(newGame.GameID());
    }

    public record CreateGameRequest(int ID, String wUsername, String bUsername, String gameName, ChessGame chessGame){}

    public record CreateGameResult(int ID){}

    void joinGame(){

    }
}
