package dataaccess;

import datamodels.UserData;

public interface UserDAO {
    void insertUser(UserData user) throws DataAccessException;

    /**
     *
     * @param username
     * @return UserData if found, null if none
     * @throws DataAccessException in cases of SQL issues
     */
    UserData getUser(String username) throws DataAccessException;
    void clear() throws DataAccessException;
}
