package dataaccess;

import datamodels.AuthData;

import java.util.HashMap;

public class MemAuthDAO implements AuthDAO{
    private static final HashMap<String, AuthData> AUTH_DB = new HashMap<>();

    @Override
    public AuthData insertAuth(String username) {
        AuthData data = new AuthData(username, AuthDAO.generateToken());
        AUTH_DB.put(data.authToken(), data);
        return data;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return AUTH_DB.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        AUTH_DB.remove(authToken);
    }

    @Override
    public void clear() {
        AUTH_DB.clear();
    }
}
