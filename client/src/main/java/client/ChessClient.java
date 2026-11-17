package client;

import chess.ChessGame;
import model.AuthData;
import service.GameService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

public class ChessClient {

    private String currentUser;
    private ChessGame currentGame;
    private boolean isWhitePerspective;
    private String authToken;
    ServerFacade serverFacade;
    private boolean loggedIn;
    private boolean inGame;

    public ChessClient(int serverPort){
        this.serverFacade = new ServerFacade(serverPort);
        this.loggedIn = false;
        this.inGame = false;
    }

    public String eval(String input) throws Exception {
        var token = input.toLowerCase().split(" ");
        var cmd = (token.length > 0) ? token[0] : "help";
        var params = Arrays.copyOfRange(token, 1, token.length);

        if(!loggedIn) {
            return switch (cmd) {
                case "register" -> helpRegister(params);
                case "login" -> helpLogin(params);
                case "quit" -> "quit";
                case "help" -> help();
                default -> "Unknown command. Type 'help'.\n";
            };
        } else {
            if(!inGame) {
                return switch (cmd) {
                    case "creategame" -> helpCreateGame(params);
                    case "joingame" -> helpJoinGame(params);
                    case "listgames" -> helpListGames(params);
                    case "logout" -> helpLogout(params);
                    case "quit" -> "quit";
                    case "help" -> help();
                    default -> "Unknown command. Type 'help'.\n";
                };
            }
            else {
                return switch (cmd) {
                    case "redraw" -> helpRedraw(params);
                    case "leave" -> helpLeave(params);
                    case "quit" -> "quit";
                    case "help" -> help();
                    default -> "Unknown command. Type 'help'.\n";
                };
            }
        }
    }

    private String helpLeave(String[] params) {
        if(params.length != 0){
            return "Expected: leave\n";
        }
        inGame = false;
        return "Left game\n";
    }

    private String helpRedraw(String[] params) {
        return BoardDrawer.drawBoard(currentGame.getBoard(), isWhitePerspective);
    }

    private String helpLogout(String[] params) throws Exception {
        if(params.length != 0){
            return "Expected: logout\n";
        }
        serverFacade.logout(authToken);
        String loggedOutUser = currentUser;
        this.currentUser = null;
        this.authToken = null;
        try {
            loggedIn = false;
            return String.format("Successfully logged out user %s.\n", loggedOutUser);
        } catch (Exception e){
            return "Logout failed: " + e.getMessage() + "\n";
        }
    }

    private String helpListGames(String[] params) {
        if(params.length != 0){
            return "Expected: listgames\n";
        }
        try {
            Collection<GameService.GameInfo> games = serverFacade.listGame(authToken);
            if (games.isEmpty()){
                return "No games are available.\n";
            }
            StringBuilder built = createBuilt(games);
            return built.toString();
        } catch (Exception e){
            return "Listing games failed: " + e.getMessage() + "\n";
        }
    }

    private static StringBuilder createBuilt(Collection<GameService.GameInfo> games) {
        StringBuilder built = new StringBuilder();
        built.append("---- Current Games ----\n");
        int id = 1;
        for(var game: games){
            built.append(String.format("%d %s (White: %s, Black: %s)\n",
                    id++,
                    game.gameName(),
                    game.whiteUsername() != null ? game.whiteUsername() : "<empty>",
                    game.blackUsername() != null ? game.blackUsername() : "<empty>"));
        }
        return built;
    }

    private String helpJoinGame(String[] params) {
        if(params.length != 2){
            return "Expected: joingame <playerColor> <gameID>\n";
        }

        String playerColor = params[0].toUpperCase();
        int gameID = Integer.parseInt(params[1]);

        try {
            if (!playerColor.equals("OBSERVER")){
                serverFacade.joinGame(playerColor,gameID,authToken);
            }

            this.currentGame = new ChessGame();
            this.isWhitePerspective = playerColor.equals("WHITE");
            this.inGame = true;

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Successfully joined game: %s.\n", gameID));
            sb.append(BoardDrawer.drawBoard(currentGame.getBoard(), isWhitePerspective));
            return sb.toString();
        } catch (Exception e){
            return "Game joining failed: " + e.getMessage() + "\n";
        }
    }

    private String helpCreateGame(String[] params) throws Exception {
        if(params.length != 1){
            return "Expected: creategame <gameName>\n";
        }

        String gameName = params[0];

        try {
            serverFacade.createGame(gameName,authToken);
            return String.format("Successfully created game: %s.\n", gameName);
        } catch (Exception e){
            return "Game creation failed: " + e.getMessage() + "\n";
        }
    }

    public String helpRegister(String[] params) throws Exception {
        if(params.length != 3){
            return "Expected: register <username> <password> <email>\n";
        }

        String username = params[0];
        String password = params[1];
        String email = params[2];

        try {
            AuthData auth = serverFacade.register(username,password,email);
            this.authToken = auth.authToken();
            this.currentUser = auth.username();
            loggedIn = true;
            return String.format("Successfully registered user %s.\n", currentUser);
        } catch (Exception e){
            return "Registration failed: " + e.getMessage() + "\n";
        }
    }

    public String helpLogin(String[] params) throws Exception{
        if(params.length != 2){
            return "Expected: login <username> <password>\n";
        }

        String username = params[0];
        String password = params[1];

        try {
            AuthData auth = serverFacade.login(username,password);
            this.authToken = auth.authToken();
            this.currentUser = auth.username();
            loggedIn = true;
            return String.format("Successfully logged in user %s.\n", currentUser);
        } catch (Exception e) {
            return "Login failed: " + e.getMessage() + "\n";
        }
    }

    public String help() throws Exception {
        if (!loggedIn) {
            return "Use one of the following commands: register <username> <password> <email>  login <username> <password> quit";
        }
        else {
            if (!inGame) {
                return "Use one of the following commands: listgames  creategame  joingame  logout  quit";
            }
            else {
                return "Use one of the following commands: redraw leave quit";
            }
        }
    }
}
