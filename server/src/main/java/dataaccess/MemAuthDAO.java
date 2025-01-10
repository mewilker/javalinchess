package dataaccess;

import datamodels.AuthData;

import java.util.HashMap;

public class MemAuthDAO implements AuthDAO{
    private static final HashMap<String, AuthData> authDB = new HashMap<>();

    @Override
    public AuthData insertAuth(String username) {
        AuthData data = new AuthData(username, AuthDAO.generateToken());
        authDB.put(data.authToken(), data);
        return data;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return authDB.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        authDB.remove(authToken);
    }

    @Override
    public void clear() {
        authDB.clear();
    }
}
