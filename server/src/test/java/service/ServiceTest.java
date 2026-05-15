package service;

import chess.ChessGame;
import dataaccess.*;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;
import response.*;
import requests.*;
import datamodels.*;

import java.util.ArrayList;

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
    @DisplayName("Repeat Register")
    public void registerRepeat() throws DataAccessException {
        RegisterService service = new RegisterService(userdao, authdao);
        Result result = service.register(new RegisterRequest("username", "password", "email"));
        Result message = new Result();
        message.setMessage("Error: Username already taken");
        Assertions.assertEquals(message.getMessage(), result.getMessage());
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
    @DisplayName ("Wrong Password")
    public void invalidPassword() throws DataAccessException{
        LoginService service = new LoginService(userdao, authdao);
        Result result = service.login(new LoginRequest("C3PO", "skywalkers"));
        Result message = new Result();
        message.setMessage("Error: unauthorized access");
        Assertions.assertEquals(message.getMessage(), result.getMessage());
    }

    @Test
    @DisplayName ("Logout")
    public void logout() throws DataAccessException{
        LogoutService service = new LogoutService(authdao);
        Result result = service.logout(new LogoutRequest(token));
        Assertions.assertNull(result.getMessage());
    }

    @Test
    @DisplayName("Logout with dead AuthToken")
    public void badlogout() throws DataAccessException{
        LogoutService service = new LogoutService(authdao);
        Result result = service.logout(new LogoutRequest("aaa"));
        Result message = new Result();
        message.setMessage("Error: unauthorized");
        Assertions.assertEquals(message.getMessage(), result.getMessage());
    }

    @Test
    @DisplayName("List Games")
    public void listGames() throws DataAccessException{
        ListGamesService service = new ListGamesService(gamedao);
        ListGamesResult result = (ListGamesResult) service.list();
        Assertions.assertEquals(gamedao.getAllGames(), result.getGames());
    }

    @Test
    @DisplayName("List No Games")
    public void unauthorizedGames() throws DataAccessException{
        gamedao.clear();
        ListGamesService service = new ListGamesService(gamedao);
        ListGamesResult result = (ListGamesResult) service.list();
        Assertions.assertEquals(new ArrayList<GameData>(), result.getGames());
    }

    @Test
    @DisplayName("Create Game")
    public void makeGame() throws DataAccessException{
        CreateGameService service = new CreateGameService(gamedao);
        CreateGameResult result = (CreateGameResult) service.createGame(new CreateGameRequest("Dejarik", token));
        Assertions.assertNotNull(gamedao.getGame(result.getGameID()));
    }

    @Test
    @DisplayName("Create Game blank")
    public void cantMakeGame()throws DataAccessException{
        CreateGameService service = new CreateGameService(gamedao);
        Result result = service.createGame(new CreateGameRequest( "", "aaa"));
        Result message = new Result();
        message.setMessage("Error: bad request");
        Assertions.assertEquals(message.getMessage(), result.getMessage());
    }


    @Test
    @DisplayName("Join Game")
    public void join() throws DataAccessException{
        JoinGameService service = new JoinGameService(authdao, gamedao);
        Result result = service.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, gameid, token));
        Assertions.assertNull(result.getMessage());
        Assertions.assertEquals("username", gamedao.getGame(gameid).blackUsername());
    }

    @Test
    @DisplayName("No spots open")
    public void gameFull() throws DataAccessException{
        RegisterService register = new RegisterService(userdao, authdao);
        var response = (LoginResult) register.register(new RegisterRequest("R2", "R2", "R2"));
        JoinGameService service = new JoinGameService(authdao, gamedao);
        service.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, gameid, response.getAuthToken()));
        Result result = service.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, gameid,token));
        Result message = new Result();
        message.setMessage("Error: black side already taken");
        Assertions.assertEquals(message.getMessage(), result.getMessage());
    }

}
