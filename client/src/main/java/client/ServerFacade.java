package client;

import model.AuthData;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import com.google.gson.Gson;

import java.util.Collection;

public class ServerFacade {
    private final String serverURL;
    private static final Gson gson = new Gson();
    public ServerFacade(int port){
        this.serverURL = "http://localhost:" + port;
    }


    public AuthData login(String username, String password) throws Exception {
        URL url = new URI(serverURL + "/session").toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json");

        LoginRequest request = new LoginRequest(username, password);
        String jsonBody = gson.toJson(request);
        try (OutputStream os = http.getOutputStream()){
            os.write(jsonBody.getBytes());
        }

        if (http.getResponseCode() != 200){
            throw new Exception("Credentials not found");
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

        RegisterRequest request = new RegisterRequest(username, password, email);
        String jsonBody = gson.toJson(request);
        try (OutputStream os = http.getOutputStream()){
            os.write(jsonBody.getBytes());
        }

        if (http.getResponseCode() != 200){
            throw new Exception("User already taken");
        }
        try (InputStream is = http.getInputStream()){
            return gson.fromJson(new InputStreamReader(is), AuthData.class);
        }
    }

    public void logout(String authToken) throws Exception {
        URL url = new URI(serverURL + "/session").toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("DELETE");
        http.setRequestProperty("Authorization", authToken);
        http.connect();

        if (http.getResponseCode() != 200){
            throw new Exception("Missing Authorization");
        }
    }

    public int createGame(String gameName, String authToken) throws Exception {
        URL url = new URI(serverURL + "/game").toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json");
        http.setRequestProperty("Authorization", authToken);

        CreateGameRequest request = new CreateGameRequest(gameName);
        String jsonBody = gson.toJson(request);
        try (OutputStream os = http.getOutputStream()){
            os.write(jsonBody.getBytes());
        }

        if (http.getResponseCode() != 200){
            throw new Exception("Could not create game");
        }
        try (InputStream is = http.getInputStream()){
            CreateGameResult result = gson.fromJson(new InputStreamReader(is), CreateGameResult.class);
            return result.gameID();
        }
    }

    public Collection<GameInfo> listGame(String authToken) throws Exception {
        URL url = new URI(serverURL + "/game").toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("GET");
        http.setRequestProperty("Authorization", authToken);
        http.connect();

        if (http.getResponseCode() != 200){
            throw new Exception("Could not list games");
        }
        try (InputStream is = http.getInputStream()){
            ListGameResult result = gson.fromJson(new InputStreamReader(is), ListGameResult.class);
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

        JoinGameRequest request = new JoinGameRequest(playerColor,gameID);
        String jsonBody = gson.toJson(request);
        try (OutputStream os = http.getOutputStream()){
            os.write(jsonBody.getBytes());
        }

        if (http.getResponseCode() != 200){
            throw new Exception("Cannot join this game with this player color");
        }
    }

    public void clear() throws Exception {
        URL url = new URI(serverURL + "/db").toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("DELETE");
        http.connect();

        if (http.getResponseCode() != 200){
            throw new Exception("Cannot clear");
        }
    }
}
