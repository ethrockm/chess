package dataaccess;

import model.UserData;
import java.util.HashMap;


public class UserDAO {
    private static final HashMap<String, UserData> users = new HashMap<>();
    public void createUser(UserData user) throws DataAccessException{
        users.put(user.username(),user);
    }
    public static UserData getUser(String username) throws DataAccessException{
        return users.get(username);
    }

    public void clearUsers() throws DataAccessException{
        users.clear();
    }

}
