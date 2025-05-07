package dataaccess;

import datamodels.UserData;

import java.util.HashMap;

public class MemUserDAO implements UserDAO {
    public static final HashMap<String, UserData> USER_DB = new HashMap<>();

    @Override
    public void insertUser(UserData user) {
        USER_DB.put(user.username(), user);
    }

    @Override
    public UserData getUser(String username) {
        return USER_DB.get(username);
    }

    @Override
    public void clear() {
        USER_DB.clear();
    }
}
