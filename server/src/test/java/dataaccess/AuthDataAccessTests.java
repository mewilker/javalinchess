package dataaccess;

import datamodels.AuthData;
import org.junit.jupiter.api.*;

public class AuthDataAccessTests {
    AuthDAO adao;
    AuthData token;

    @BeforeAll
    public static void dbSetup() throws DataAccessException {
        DatabaseManager.createDatabase();
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        adao = new SQLAuthDAO();
        adao.clear();
        token = adao.insertAuth("C3PO");
    }

    @Test
    @DisplayName("Positive Clear Test")
    public void clear() throws DataAccessException{
        adao.clear();
        Assertions.assertNull(adao.getAuth(token.authToken()));
    }

    @Test
    @DisplayName("Positive Insert")
    public void insert() throws DataAccessException{
        AuthData data = adao.insertAuth("R2-D2");
        Assertions.assertEquals(adao.getAuth(data.authToken()), data);
    }

    @Test
    @DisplayName("Positive Get")
    public void get() throws DataAccessException{
        AuthData data = adao.getAuth(token.authToken());
        Assertions.assertEquals(token, data);
    }

    @Test
    @DisplayName("Positive delete")
    public void deleteOne() throws DataAccessException {
        var deleteMe = adao.insertAuth("R2-D2");
        Assertions.assertNotNull(adao.getAuth(deleteMe.authToken()));
        adao.deleteAuth(deleteMe.authToken());
        Assertions.assertNull(adao.getAuth(deleteMe.authToken()));
        Assertions.assertNotNull(adao.getAuth(token.authToken()));
    }
}
