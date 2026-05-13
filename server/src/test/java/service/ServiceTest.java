package service;

import chess.ChessGame;
import dataaccess.*;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;
import response.*;
import requests.*;
import services.*;
import datamodels.*;

public class ServiceTest {

    private static AuthDAO authdao;
    private static UserDAO userdao;
    private static GameDAO gamedao;

    private String token;
    private int gameid;

    @BeforeAll
    public static void dbInit() throws DataAccessException{
        authdao = new MemAuthDAO();
        userdao = new MemUserDAO();
        gamedao = new MemGameDAO();
    }

    @BeforeEach
    public void setup() throws DataAccessException{
        gamedao.clear();
        authdao.clear();
        userdao.clear();
        userdao.insertUser(new UserData("username", "password", "email"));
        token = authdao.insertAuth("username").authToken();
        userdao.insertUser(new UserData("me", "password", "email"));
        authdao.insertAuth("me");
        userdao.insertUser(new UserData("C3PO", BCrypt.hashpw("droid", BCrypt.gensalt()),"Threepio@jeditemple.com"));
        gamedao.insertGame("gamegame");
        gamedao.insertGame("game");
        gameid = gamedao.insertGame("Sabaac");

    }

    @Test
    @DisplayName("Positive Clear Test")
    public void clearTest() throws DataAccessException{
        ClearService service = new ClearService(userdao, authdao, gamedao);
        Assertions.assertDoesNotThrow(service::clear);
    }

    @Test
    @DisplayName("Register")
    public void registerNew() throws DataAccessException {
        RegisterService service = new RegisterService(userdao, authdao);
        LoginResult result = (LoginResult) service.register(new RegisterRequest(
                "R2-D2",
                "skywalkers",
                "Artoo@jeditemple.com"
        ));
        Assertions.assertNotNull(result.getAuthToken());
        Assertions.assertEquals("R2-D2", result.getUsername());
        Assertions.assertEquals(authdao.getAuth(result.getAuthToken()).username(), result.getUsername());
    }

    @Test
    @DisplayName ("Login")
    public void login() throws DataAccessException{
        LoginService service = new LoginService(userdao, authdao);
        LoginResult result = (LoginResult) service.login(new LoginRequest("C3PO","droid"));
        Assertions.assertNotNull(result.getAuthToken());
        Assertions.assertEquals("C3PO", result.getUsername());
        Assertions.assertEquals(authdao.getAuth(result.getAuthToken()).username(), result.getUsername());
    }

    @Test
    @DisplayName ("Logout")
    public void logout() throws DataAccessException{
        LogoutService service = new LogoutService(authdao);
        Result result = service.logout(new LogoutRequest(token));
        Assertions.assertNull(result.getMessage());
    }

    @Test
    @DisplayName("List Games")
    public void listGames() throws DataAccessException{
        ListGamesService service = new ListGamesService(gamedao);
        ListGamesResult result = (ListGamesResult) service.list();
        Assertions.assertEquals(gamedao.getAllGames(), result.getGames());
    }

    @Test
    @DisplayName("Create Game")
    public void makeGame() throws DataAccessException{
        CreateGameService service = new CreateGameService(gamedao);
        CreateGameResult result = (CreateGameResult) service.createGame(new CreateGameRequest("Dejarik", token));
        Assertions.assertNotNull(gamedao.getGame(result.getGameID()));
    }

    @Test
    @DisplayName("Join Game")
    public void join() throws DataAccessException{
        JoinGameService service = new JoinGameService(authdao, gamedao);
        Result result = service.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, gameid, token));
        Assertions.assertNull(result.getMessage());
        Assertions.assertEquals("username", gamedao.getGame(gameid).blackUsername());
    }
}
