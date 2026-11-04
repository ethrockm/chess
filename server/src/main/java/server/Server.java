package server;

import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;
import service.ClearService;
import service.GameService;
import service.UserService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class Server {
    private UserService userService;
    private GameService gameService;
    private final Gson gson = new Gson();
    private final Javalin javalin;
    private ClearService clearService;

    public Server() {

        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        // Initialize DAO and Service
        try {
            UserDAO userDAO = new SQLUserDAO();
            AuthDAO authDAO = new SQLAuthDAO();
            GameDAO gameDAO = new SQLGameDAO();
            this.userService = new UserService(userDAO, authDAO);
            this.gameService = new GameService(gameDAO, authDAO);
            this.clearService = new ClearService(userDAO, gameDAO, authDAO);
        } catch (dataaccess.DataAccessException e) {
            System.out.printf("Server failed to start: %s%n", e.getMessage());
        }

        // Register endpoints
        javalin.post("/user", this::registerHandler);
        javalin.post("/session", this::loginHandler);
        javalin.delete("/session", this::logoutHandler);
        javalin.delete("/db", this::clearHandler);
        javalin.post("/game", this::createGameHandler);
        javalin.get("/game", this::listGameHandler);
        javalin.put("/game", this::joinGameHandler);

        // Global Exception Handler
        javalin.exception(Exception.class, (e, req) -> {
            String message = e.getMessage();
            if (message == null) {
                message = "Error: Internal server error";
            }

            // Route the exception to the proper HTTP status code
            if (message.contains("bad request")) {
                req.status(400);
            } else if (message.contains("unauthorized")) {
                req.status(401);
            } else if (message.contains("already taken")) {
                req.status(403);
            } else {
                req.status(500);
            }

            // Serialize the error using Gson
            req.result(gson.toJson(Map.of("message", message)));
        });
    }

    private void configureDatabase(){
        try{
            DatabaseManager.createDatabase();
        } catch (Exception e){
            System.out.printf("Unable to configure database: %s%n", e.getMessage());
        }
    }

    private void joinGameHandler(Context ctx){
        try {
            String authToken = ctx.header("authorization");
            GameService.JoinGameRequest request = gson.fromJson(ctx.body(), GameService.JoinGameRequest.class);
            if(request == null){
                throw new DataAccessException("Error: bad request");
            }
            gameService.joinGame(request,authToken);
            ctx.status(200).result("{}");
        } catch (DataAccessException | SQLException e) {
            if(e.getMessage().equals("Error: bad request")){
                ctx.status(400).result(gson.toJson(Map.of("message", e.getMessage())));
            } else if(e.getMessage().equals("Error: unauthorized")){
                ctx.status(401).result(gson.toJson(Map.of("message", e.getMessage())));
            } else if(e.getMessage().equals("Error: already taken")){
                ctx.status(403).result(gson.toJson(Map.of("message", e.getMessage())));
            }else {
                ctx.status(500).result(gson.toJson(Map.of("message","Error: " + e.getMessage())));
            }
        }
    }

    private void createGameHandler(Context ctx){
        try {
            String authToken = ctx.header("authorization");
            GameService.CreateGameRequest request = gson.fromJson(ctx.body(), GameService.CreateGameRequest.class);
            GameService.CreateGameResult result = gameService.createGame(request,authToken);
            ctx.status(200).result(gson.toJson(result));
        } catch (DataAccessException | SQLException e) {
            if(e.getMessage().equals("Error: bad request")){
                ctx.status(400).result(gson.toJson(Map.of("message", e.getMessage())));
            } else if(e.getMessage().equals("Error: unauthorized")){
                ctx.status(401).result(gson.toJson(Map.of("message", e.getMessage())));
            } else {
                ctx.status(500).result(gson.toJson(Map.of("message","Error: " + e.getMessage())));
            }
        }
    }

    private void listGameHandler(Context ctx){
        try {
            String authToken = ctx.header("authorization");
            GameService.ListGameResult result = gameService.listGame(authToken);
            ctx.status(200).result(gson.toJson(result));
        } catch (DataAccessException e) {
            if(e.getMessage().equals("Error: unauthorized")){
                ctx.status(401).result(gson.toJson(Map.of("message", e.getMessage())));
            } else {
                ctx.status(500).result(gson.toJson(Map.of("message","Error: " + e.getMessage())));
            }
        }
    }

    private void registerHandler(Context ctx){
        try {
            UserService.RegisterRequest request = gson.fromJson(ctx.body(), UserService.RegisterRequest.class);
            UserService.RegisterResult result = userService.register(request);
            ctx.status(200).result(gson.toJson(result));
        } catch (DataAccessException | SQLException e) {
            if(e.getMessage().equals("Error: bad request")){
                ctx.status(400).result(gson.toJson(Map.of("message", e.getMessage())));
            } else if(e.getMessage().equals("Error: already taken")){
                ctx.status(403).result(gson.toJson(Map.of("message", e.getMessage())));
            } else {
                ctx.status(500).result(gson.toJson(Map.of("message","Error: " + e.getMessage())));
            }
        }
    }

    private void clearHandler(Context ctx){
        try{
            clearService.clear();
            ctx.status(200).result(gson.toJson(Map.of()));
        } catch (DataAccessException e){
            ctx.status(500).result(gson.toJson(Map.of("message","Error: " + e.getMessage())));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loginHandler(Context ctx){
        try{
            UserService.LoginRequest request = gson.fromJson(ctx.body(), UserService.LoginRequest.class);
            UserService.LoginResult result = userService.login(request);
            ctx.status(200).result(gson.toJson(result));
        } catch (DataAccessException | SQLException e){
            if(e.getMessage().equals("Error: bad request")){
                ctx.status(400).result(gson.toJson(Map.of("message", e.getMessage())));
            } else if(e.getMessage().equals("Error: unauthorized")){
                ctx.status(401).result(gson.toJson(Map.of("message", e.getMessage())));
            } else {
                ctx.status(500).result(gson.toJson(Map.of("message","Error: " + e.getMessage())));
            }
        }
    }

    private void logoutHandler(Context ctx){
        try{
            String authToken = ctx.header("authorization");
            userService.logout(authToken);
            ctx.status(200).result("{}");
        } catch (DataAccessException | SQLException e){
            if(e.getMessage().equals("Error: unauthorized")){
                ctx.status(401).result(gson.toJson(Map.of("message", e.getMessage())));
            } else {
                ctx.status(500).result(gson.toJson(Map.of("message","Error: " + e.getMessage())));
            }
        }
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}