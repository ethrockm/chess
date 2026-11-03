package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    private static final HashMap<String, AuthData> auths = new HashMap<>();

    @Override
    public AuthData createAuth(String username){
        String token = UUID.randomUUID().toString();
        AuthData authData = new AuthData(token,username);
        auths.put(token, authData);
        return authData;
    }

    @Override
    public AuthData getAuth(String username) {
        return auths.get(username);
    }

    @Override
    public void deleteAuth(String username){
        auths.remove(username);
    }

    @Override
    public void clear(){
        auths.clear();
    }
}
