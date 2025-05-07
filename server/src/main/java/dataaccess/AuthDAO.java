package dataaccess;

import datamodels.AuthData;

import java.util.UUID;

public interface AuthDAO {
    static String generateToken() {
        return UUID.randomUUID().toString();
    }

    AuthData insertAuth(String username) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;

    void clear() throws DataAccessException;
}
