package dataaccess;

import chess.ChessGame;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class SQLDataAccessTest {
    private MySqlDataAccess dataAccess;

    @BeforeEach
    void setup() throws Exception {
        dataAccess = new MySqlDataAccess();
        dataAccess.clear();
    }

    @Test
    void getUser() throws DataAccessException {
        var user = new UserData("BagelBro", "Hotdog95", "BagelBroman@gmail.com");
        dataAccess.createUser(new UserData(user.username(),user.password(), user.email()));

        var result = dataAccess.getUser(user.username());
        assertNotNull(result);

        Collection<UserData> users = dataAccess.listUsers();
        assertEquals(1, users.size());
    }

    @Test
    void duplicateUser() throws DataAccessException {
        var user = new UserData("BagelBro", "Hotdog95", "BagelBroman@gmail.com");
        dataAccess.createUser(new UserData(user.username(),user.password(), user.email()));

        var result = dataAccess.getUser(user.username());
        assertNotNull(result);

        assertThrows(DataAccessException.class,
                () -> dataAccess.createUser(new UserData(user.username(),user.password(), user.email()))
        );

        Collection<UserData> users = dataAccess.listUsers();
        assertEquals(1, users.size());
    }

    @Test
    public void createGameTest() throws DataAccessException{
        var user = new UserData("BagelBro", "Hotdog95", "BagelBroman@gmail.com");
        dataAccess.createUser(new UserData(user.username(),user.password(), user.email()));
        var newCreateGame = new GameData(0, null,
                null, "aGameYeah", new ChessGame());

        assertDoesNotThrow(
                () -> dataAccess.createGame(newCreateGame)
        );

        Collection<GameData> gamesListResult = assertDoesNotThrow(
                () -> dataAccess.listGames()
        );
        assertEquals(1, gamesListResult.size());
    }

    @Test
    public void getGameTest() throws DataAccessException{
        var user = new UserData("BagelBro", "Hotdog95", "BagelBroman@gmail.com");
        dataAccess.createUser(new UserData(user.username(),user.password(), user.email()));
        var newCreateGame = new GameData(0, null,
                null, "aGameYeah", new ChessGame());

        var gameID = assertDoesNotThrow(
                () -> dataAccess.createGame(newCreateGame)
        );

        var gameGet = assertDoesNotThrow(
                () -> dataAccess.getGame(gameID)
        );

        assertEquals(newCreateGame, gameGet);
    }

    @Test
    void getGameInvalidID() throws DataAccessException {
        assertNull(dataAccess.getGame(-1));
    }

    @Test
    void updateGameTest() throws DataAccessException {
        var user = new UserData("BagelBro", "Hotdog95", "BagelBroman@gmail.com");
        dataAccess.createUser(new UserData(user.username(),user.password(), user.email()));
        var newCreateReq = new GameData(0, null,
                null, "aGameYeah", new ChessGame());

        var gameID = assertDoesNotThrow(
                () -> dataAccess.createGame(newCreateReq)
        );
        var updated = new GameData(gameID, "pizza",
                null, "aGameYeah", new ChessGame());
        assertDoesNotThrow(
                () -> dataAccess.updateGame(updated)
        );

        Collection<GameData> gamesListResult = assertDoesNotThrow(
                () -> dataAccess.listGames()
        );
        assertEquals(1, gamesListResult.size());
        GameData result = gamesListResult.stream().findFirst().orElseThrow();
        assertEquals("pizza", result.whiteUsername());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SQLDataAccessTest that = (SQLDataAccessTest) o;
        return Objects.equals(dataAccess, that.dataAccess);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dataAccess);
    }
}
