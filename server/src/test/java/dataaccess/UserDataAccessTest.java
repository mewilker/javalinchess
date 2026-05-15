package dataaccess;

import dataaccess.sql.SQLUserDAO;
import datamodels.UserData;
import org.junit.jupiter.api.*;

public class UserDataAccessTest {
    UserDAO udao;
    UserData user = new UserData("C3PO", "droid", "threepio@jeditemple.com");

    @BeforeAll
    public static void dbSetup() throws DataAccessException{
        DatabaseManager.createDatabase();
    }

    @BeforeEach
    public void setup() throws DataAccessException{
        udao = new SQLUserDAO();
        udao.clear();
        udao.insertUser(user);
    }

    @Test
    @DisplayName("Positive Clear Test")
    public void clear() throws DataAccessException{
        udao.clear();
        Assertions.assertNull(udao.getUser(user.username()));
    }

    @Test
    @DisplayName("Positive Insert")
    public void insert() throws DataAccessException{
        UserData artoo =new UserData("R2-D2", "skywalkers", "Artoo@jeditemple.com");
        udao.insertUser(artoo);
        Assertions.assertEquals(artoo, udao.getUser(artoo.username()));
    }

    @Test
    public void failInsert() {
        Assertions.assertThrows(DataAccessException.class,
                ()-> udao.insertUser(new UserData(null, null,null)));
    }

    @Test
    @DisplayName("Positive Get")
    public void get() throws DataAccessException{
        UserData threepio = udao.getUser(user.username());
        Assertions.assertEquals(user, threepio);
    }

    @Test
    public void failGet() throws DataAccessException {
        var got = udao.getUser("DoesNotExist");
        Assertions.assertNull(got);
    }

}
