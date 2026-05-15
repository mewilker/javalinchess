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

    @Test
    @DisplayName("Positive Register")
    public void goodRegister() throws ServerErrorException {
        facade.register("Artoo", "anakin", "r2d2@jeditemple.com");
        Assertions.assertNotNull(facade.listGames()); // valid token received
    }

    @Test
    @DisplayName("Negative Register")
    public void badRegister() throws ServerErrorException{
        goodRegister();
        Assertions.assertThrows(ServerErrorException.class,
                ()-> facade.register("Artoo", "r2", "r2"));
    }

    @Test
    @DisplayName("Positive Login")
    public void goodLogin()throws ServerErrorException {
        goodRegister();
        facade.login("Artoo", "anakin");
        Assertions.assertNotNull(facade.listGames()); // valid token received
    }

    @Test
    @DisplayName("Negative Login")
    public void badLogin()throws ServerErrorException{
        goodRegister();
        Assertions.assertThrows(ServerErrorException.class, ()-> facade.login("Artoo", "skywalker"));

    }

    @Test
    @DisplayName("Positive Logout")
    public void goodLogout()throws ServerErrorException{
        facade.register("Artoo", "anakin", "r2d2@jeditemple.com");
        Assertions.assertDoesNotThrow(()->facade.logout());
    }

    @Test
    @DisplayName("Negative Logout")
    public void badLogout(){
        Assertions.assertThrows(ServerErrorException.class,()->facade.logout());
    }

    @Test
    @DisplayName("Positive Create Game")
    public void goodCreate() throws ServerErrorException{
        facade.register("Artoo", "anakin", "r2d2@jeditemple.com");
        int id = facade.createGame("sabaac");
        Assertions.assertTrue(id > 0);
    }

    @Test
    @DisplayName("Negative Create Game")
    public void badCreate(){
        Assertions.assertThrows(ServerErrorException.class,()->facade.createGame("sabaac"));
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
    @DisplayName("Negative List Game")
    public void badList(){
        Assertions.assertThrows(ServerErrorException.class,()->facade.listGames());
    }

    @Test
    @DisplayName("Positive Join Game")
    public void goodJoin() throws ServerErrorException{
        facade.register("Artoo", "anakin", "r2d2@jeditemple.com");
        int id = facade.createGame("sabaac");
        Assertions.assertDoesNotThrow(()->facade.joinGame(ChessGame.TeamColor.BLACK, id));
    }

    @Test
    @DisplayName("Negative Join Game")
    public void badJoin() throws ServerErrorException{
        facade.register("Artoo", "anakin", "r2d2@jeditemple.com");
        int id = facade.createGame("sabaac");
        facade.joinGame(ChessGame.TeamColor.BLACK, id);
        facade.register("Vader", "Darth", "sith@empire.com");
        Assertions.assertThrows(ServerErrorException.class, ()->facade.joinGame(ChessGame.TeamColor.BLACK,id));
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

}
