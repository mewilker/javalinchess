package dataaccess;

import datamodels.UserData;

import java.util.HashMap;

public class MemUserDAO implements UserDAO{
    public static final HashMap<String, UserData> userDB = new HashMap<>();

    @Override
    public void insertUser(UserData user) {
        userDB.put(user.username(), user);
    }

    @Override
    public UserData getUser(String username) {
        return userDB.get(username);
    }

    @Override
    public void clear() {
        userDB.clear();
    }
}
