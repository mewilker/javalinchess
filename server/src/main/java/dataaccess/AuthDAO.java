package dataaccess;

import datamodels.AuthData;

import java.util.UUID;

public interface AuthDAO {
    public AuthData insertAuth(String username) throws DataAccessException;
    public AuthData getAuth(String authToken) throws DataAccessException;
    public void deleteAuth(String authToken) throws DataAccessException;
    public void clear() throws DataAccessException;
    static String generateToken(){
        return UUID.randomUUID().toString();
    }
}
