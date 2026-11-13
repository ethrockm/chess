package client;

import model.AuthData;
import model.GameData;
import service.GameService;
import service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class ServerFacade {
    private String serverURL;
    private final Gson gson = new Gson();
    public ServerFacade(int port){
        serverURL = "https://localhost" + port;
    }

    public AuthData login(String username, String password) throws Exception {
        URL url = new URI(serverURL + "/session").toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json");

        UserService.LoginRequest request = new UserService.LoginRequest(username, password);
        String jsonBody = gson.toJson(request);
        try (OutputStream os = http.getOutputStream()){
            os.write(jsonBody.getBytes());
        }

        http.connect();

        if (http.getResponseCode() != 200){
            throw new Exception("Server error code: " + http.getResponseCode());
        }
        try (InputStream is = http.getInputStream()){
            return gson.fromJson(new InputStreamReader(is), AuthData.class);
        }
    }

    public AuthData register(String username, String password, String email) throws Exception {
        URL url = new URI(serverURL + "/user").toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json");

        UserService.RegisterRequest request = new UserService.RegisterRequest(username, password, email);
        String jsonBody = gson.toJson(request);
        try (OutputStream os = http.getOutputStream()){
            os.write(jsonBody.getBytes());
        }

        http.connect();

        if (http.getResponseCode() != 200){
            throw new Exception("Server error code: " + http.getResponseCode());
        }
        try (InputStream is = http.getInputStream()){
            return gson.fromJson(new InputStreamReader(is), AuthData.class);
        }
    }

    public void logout(String authToken) throws Exception {
        URL url = new URI(serverURL + "/session").toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("DELETE");
        http.setDoOutput(true);
        http.setRequestProperty("Authorization", authToken);
        http.connect();

        if (http.getResponseCode() != 200){
            throw new Exception("Server error code: " + http.getResponseCode());
        }
    }

    public int createGame(String gameName, String authToken) throws Exception {
        URL url = new URI(serverURL + "/game").toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json");
        http.setRequestProperty("Authorization", authToken);

        GameService.CreateGameRequest request = new GameService.CreateGameRequest(gameName);
        String jsonBody = gson.toJson(request);
        try (OutputStream os = http.getOutputStream()){
            os.write(jsonBody.getBytes());
        }

        http.connect();

        if (http.getResponseCode() != 200){
            throw new Exception("Server error code: " + http.getResponseCode());
        }
        try (InputStream is = http.getInputStream()){
            GameService.CreateGameResult result = gson.fromJson(new InputStreamReader(is), GameService.CreateGameResult.class);
            return result.gameID();
        }
    }

    public Collection<GameService.GameInfo> listGame(String authToken) throws Exception {
        URL url = new URI(serverURL + "/game").toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("GET");
        http.setRequestProperty("Authorization", authToken);
        http.connect();

        if (http.getResponseCode() != 200){
            throw new Exception("Server error code: " + http.getResponseCode());
        }
        try (InputStream is = http.getInputStream()){
            GameService.ListGameResult result = gson.fromJson(new InputStreamReader(is), GameService.ListGameResult.class);
            return result.games();
        }
    }

    public void joinGame(String playerColor, int gameID, String authToken) throws Exception {
        URL url = new URI(serverURL + "/game").toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("PUT");
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json");
        http.setRequestProperty("Authorization", authToken);

        GameService.JoinGameRequest request = new GameService.JoinGameRequest(playerColor,gameID);
        String jsonBody = gson.toJson(request);
        try (OutputStream os = http.getOutputStream()){
            os.write(jsonBody.getBytes());
        }

        http.connect();

        if (http.getResponseCode() != 200){
            throw new Exception("Server error code: " + http.getResponseCode());
        }
    }

    public void clear() throws Exception {
        URL url = new URI(serverURL + "/db").toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("DELETE");
        http.setDoOutput(true);
        http.connect();

        if (http.getResponseCode() != 200){
            throw new Exception("Server error code: " + http.getResponseCode());
        }
    }
}
