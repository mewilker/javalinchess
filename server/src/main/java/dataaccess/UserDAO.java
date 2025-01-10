package dataaccess;

import datamodels.UserData;

public interface UserDAO {
    public void insertUser(UserData user) throws DataAccessException;

    /**
     *
     * @param username
     * @return UserData if found, null if none
     * @throws DataAccessException in cases of SQL issues
     */
    public UserData getUser(String username) throws DataAccessException;
    public void clear() throws DataAccessException;
}
