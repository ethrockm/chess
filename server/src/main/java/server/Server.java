package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import model.UserData;
import dataaccess.UserDAO;
import service.UserService;

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
        //do exceptions here
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
