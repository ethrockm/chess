package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class AuthDAO {
    private static final HashMap<String, AuthData> auths = new HashMap<>();

    public static AuthData createAuth(String username){
        String token = generateToken();
        AuthData authData = new AuthData(token,username);
        auths.put(token, authData);
        return authData;
    }

    public AuthData getAuth(String username) {
        return auths.get(username);
    }

    public static void deleteAuth(String username){
        auths.remove(username);
    }

    public void clear(){
        auths.clear();
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
