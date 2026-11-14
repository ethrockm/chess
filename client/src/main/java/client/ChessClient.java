package client;

import model.AuthData;

import java.util.Arrays;

public class ChessClient {

    private String currentUser;
    private String authToken;
    ServerFacade serverFacade;
    private boolean loggedIn;

    public ChessClient(int serverPort){
        this.serverFacade = new ServerFacade(serverPort);
        loggedIn = false;
    }

    public String eval(String input) throws Exception {
        var token = input.toLowerCase().split(" ");
        var cmd = (token.length > 0) ? token[0] : "help";
        var params = Arrays.copyOfRange(token, 1, token.length);

        return switch (cmd) {
            case "register" -> helpRegister(params);
            case "login" -> helpLogin(params);
            case "quit" -> "quit";

            default -> help();
        };
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
            return "Use one of the following commands: register <username> <password> <email>  login <username> <password>";
        } else {
            return "Use one of the following commands: listGames  createGame  joinGame";
        }
    }
}
