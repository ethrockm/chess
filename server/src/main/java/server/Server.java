package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.*;
import io.javalin.http.Context;
import model.UserData;
import dataaccess.UserDAO;
import service.UserService;

import java.util.Map;

public class Server {
    private final UserService userService;
    private final Gson gson = new Gson();
    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        // Initialize DAO and Service
        UserDAO userDAO = new UserDAO();
        this.userService = new UserService(userDAO);
        // Register endpoints and exception handlers
        javalin.post("/user", this::registerHandler);
    }

    private void registerHandler(Context ctx){
        try {
            UserService.RegisterRequest request = gson.fromJson(ctx.body(), UserService.RegisterRequest.class);
            UserService.RegisterResult result = userService.register(request);
            ctx.status(200).result(gson.toJson(result));
        } catch (DataAccessException e) {
            if(e.getMessage().equals("Error: bad request")){
                ctx.status(400).result(gson.toJson(Map.of("message", e.getMessage())));
            } else if(e.getMessage().equals("Error: already taken")){
                ctx.status(403).result(gson.toJson(Map.of("message", e.getMessage())));
            } else {
                ctx.status(500).result(gson.toJson(Map.of("message", "Error: (description of error? Not sure what to put here)")));
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
