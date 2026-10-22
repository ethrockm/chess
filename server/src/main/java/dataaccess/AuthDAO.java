package dataaccess;

import model.AuthData;
import java.util.UUID;

public class AuthDAO {
    String token;
    void createAuth(){
        token = generateToken();
    }

    AuthData getAuth(String username){
        return new AuthData(username, token);
    }

    void deleteAuth(){
        token = null;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
