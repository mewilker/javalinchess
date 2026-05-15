package client;

import chess.ChessGame;
import datamodels.AuthData;
import datamodels.GameData;
import facade.HttpCommunicator;
import facade.ServerErrorException;
import facade.ServerFacade;
import org.junit.jupiter.api.*;
import server.Server;

import java.net.URISyntaxException;
import java.util.Collection;


public class ServerFacadeTests {

    private static Server server;
    ServerFacade facade;
    static String url;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        url = "http://localhost:" + port;
    }

    @BeforeEach
    public void clear() throws ServerErrorException{
        facade = new ServerFacade(url);
        var http = new HttpCommunicator();
        http.doDelete(url + "/db", "");
    }

    //clear b4 each test
    @Test
    @DisplayName("Positive Register")
    public void goodRegister() throws ServerErrorException {
        facade.register("Artoo", "anakin", "r2d2@jeditemple.com");
        Assertions.assertNotNull(facade.listGames()); // valid token received
    }

    @Test
    @DisplayName("Positive Login")
    public void goodLogin()throws ServerErrorException {
        goodRegister();
        facade.login("Artoo", "anakin");
        Assertions.assertNotNull(facade.listGames()); // valid token received
    }

    @Test
    @DisplayName("Positive Logout")
    public void goodLogout()throws ServerErrorException{
        facade.register("Artoo", "anakin", "r2d2@jeditemple.com");
        Assertions.assertDoesNotThrow(()->facade.logout());
    }

    @Test
    @DisplayName("Positive Create Game")
    public void goodCreate() throws ServerErrorException{
        facade.register("Artoo", "anakin", "r2d2@jeditemple.com");
        int id = facade.createGame("sabaac");
        Assertions.assertTrue(id > 0);
    }

    @Test
    @DisplayName ("Positive List Games")
    public void goodList() throws ServerErrorException {
        facade.register("Artoo", "anakin", "r2d2@jeditemple.com");
        facade.createGame("sabaac");
        Collection<GameData> list = facade.listGames();
        Assertions.assertNotNull(list);
        Assertions.assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Positive Join Game")
    public void goodJoin() throws ServerErrorException{
        facade.register("Artoo", "anakin", "r2d2@jeditemple.com");
        int id = facade.createGame("sabaac");
        Assertions.assertDoesNotThrow(()->facade.joinGame(ChessGame.TeamColor.BLACK, id));
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

}
