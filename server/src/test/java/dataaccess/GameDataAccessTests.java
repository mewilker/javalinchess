package dataaccess;

import chess.ChessGame;
import dataaccess.sql.SQLGameDAO;
import datamodels.GameData;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collection;

public class GameDataAccessTests {
    GameDAO gdao;
    GameData game;

    @BeforeAll
    public static void dbSetup() throws DataAccessException{
        DatabaseManager.createDatabase();
    }

    @BeforeEach
    public void setup() throws DataAccessException{
        gdao = new SQLGameDAO();
        gdao.clear();
        game = new GameData(gdao.insertGame("Sabaac"),
                "Sabaac",
                null,
                null,
                new ChessGame()
        );
    }

    @Test
    @DisplayName("Positive Clear Test")
    public void clear() throws DataAccessException{
        gdao.clear();
        Assertions.assertNull(gdao.getGame(game.gameID()));
    }

    @Test
    @DisplayName("Positive Get All")
    public void getAll() throws DataAccessException{
        GameData game2 = new GameData(
                gdao.insertGame("Dejarik"),
                "Dejarik",
                null,
                null,
                new ChessGame()
        );

        Collection<GameData> games = gdao.getAllGames();
        Assertions.assertTrue(games.contains(game2));
        Assertions.assertTrue(games.contains(game));
        Assertions.assertEquals(2, games.size());
    }

    @Test
    public void getAllEmpty() throws DataAccessException{
        gdao.clear();
        Assertions.assertEquals(new ArrayList<GameData>(),gdao.getAllGames());
    }


    @Test
    @DisplayName("Positive Insert")
    public void insert() throws DataAccessException{
        int gameID = gdao.insertGame("Dejarik");
        Assertions.assertTrue(gameID > game.gameID());
        Assertions.assertEquals("Dejarik", gdao.getGame(gameID).gameName());
    }

    @Test
    @DisplayName("Negative Insert")
    public void badInsert() {
        Assertions.assertThrows(DataAccessException.class, ()-> gdao.insertGame(null));
    }

    @Test
    public void getOne() throws DataAccessException{
        GameData got = gdao.getGame(game.gameID());
        Assertions.assertEquals(game, got);
    }

    @Test
    public void failGetOne() throws DataAccessException{
        GameData got = gdao.getGame(-1);
        Assertions.assertNull(got);
    }

    @Test
    public void update() throws DataAccessException {
        GameData updated = new GameData(
                game.gameID(),
                game.gameName(),
                "Leia",
                "Luke",
                new ChessGame()
        );
        gdao.updateGame(updated);
        var got = gdao.getGame(game.gameID());
        Assertions.assertEquals(updated, got);
        Assertions.assertNotEquals(game, got);
    }

    @Test
    public void badUpdate() throws DataAccessException {
        GameData invalid = new GameData(
                game.gameID(),
                null,
                null,
                null,
                null
        );
        gdao.updateGame(invalid);
        GameData got = gdao.getGame(game.gameID());
        Assertions.assertNotNull(got.gameName());
    }
}
