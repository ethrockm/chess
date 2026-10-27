package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static dataaccess.GameDAO.getGame;
import static dataaccess.GameDAO.getGames;
import static dataaccess.UserDAO.getUser;


public class GameService {
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public CreateGameResult createGame(CreateGameRequest request, String authToken) throws DataAccessException {
        if(authDAO.getAuth(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        //If not all fields are filled, then throw exception
        if(request.gameName().isEmpty()){
            throw new DataAccessException("Error: bad request");
        }
        // Create the User
        GameData newGame = new GameData(0,null,null,request.gameName(),new ChessGame());
        int gameID = GameDAO.createGame(newGame);
        return new CreateGameResult(gameID);
    }

    public record CreateGameRequest(String gameName){}

    public record CreateGameResult(int ID){}

    public ListGameResult listGame(String authToken) throws DataAccessException {
        if(authDAO.getAuth(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        Collection<model.GameData> allGames = getGames();
        Collection<GameInfo> gamesFormatted = new ArrayList<>();
        for (model.GameData game: allGames){
            gamesFormatted.add(new GameInfo(game.GameID(),game.whiteUsername(),game.blackUsername(),game.gameName()));
        }
        return new ListGameResult(gamesFormatted);
    }

    public record GameInfo(int gameID, String whiteUsername, String blackUsername, String gameName){}
    public record ListGameResult(Collection<GameInfo> games){}


    void joinGame(){

    }
}
