package client;

import model.AuthData;
import service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;

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
}
