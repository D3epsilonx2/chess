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
    void createUser() throws DataAccessException {
        var user = new UserData("BagelBro", "Hotdog95", "BagelBroman@gmail.com");
        dataAccess.createUser(new UserData(user.username(),user.password(), user.email()));

        Collection<UserData> users = dataAccess.listUsers();
        assertEquals(1, users.size());
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
    void getNoUser() throws DataAccessException {
        var user = new UserData("BagelBro", "Hotdog95", "BagelBroman@gmail.com");

        assertNull(dataAccess.getUser(user.username()));
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
    public void createEvilGameTest() throws DataAccessException{
        var user = new UserData("BagelBro", "Hotdog95", "BagelBroman@gmail.com");
        dataAccess.createUser(new UserData(user.username(),user.password(), user.email()));
        var newCreateGame = new GameData(0, null,
                null, null, null);

        assertThrows(DataAccessException.class,
                () -> dataAccess.createGame(newCreateGame)
        );
    }

    @Test
    public void getGameTest() throws DataAccessException{
        var user = new UserData("BagelBro", "Hotdog95", "BagelBroman@gmail.com");
        dataAccess.createUser(new UserData(user.username(),user.password(), user.email()));
        var newCreateGame = new GameData(1, null,
                null, "aGameYeah", new ChessGame());

        var gameID = assertDoesNotThrow(
                () -> dataAccess.createGame(newCreateGame)
        );

        var gameGet = assertDoesNotThrow(
                () -> dataAccess.getGame(gameID)
        );

        assertEquals(newCreateGame.gameName(), gameGet.gameName());
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

    @Test
    void badUpdateGame() throws DataAccessException {
        var updated = new GameData(1, null,
                null, null, new ChessGame());
        dataAccess.updateGame(updated);
        Collection<GameData> result = dataAccess.listGames();
        assertEquals(0, result.size());
    }

    @Test
    void createAuth() throws DataAccessException {
        AuthData auth = new AuthData("33", "ham");
        assertDoesNotThrow(
                () -> dataAccess.createAuth(auth)
        );
        assertEquals(auth.username(), dataAccess.getAuth("33").username());
    }

    @Test
    void createEvilAuth() throws DataAccessException {
        AuthData auth = new AuthData(null, "ham");
        assertThrows(DataAccessException.class,
                () -> dataAccess.createAuth(auth)
        );
    }

    @Test
    void getEvilAuth() throws DataAccessException {
        AuthData auth = new AuthData("33", "ham");
        assertDoesNotThrow(
                () -> dataAccess.createAuth(auth)
        );
        assertNull(dataAccess.getAuth("32"));
    }

    @Test
    void deleteAuth() throws DataAccessException {
        AuthData auth = new AuthData("33", "ham");
        assertDoesNotThrow(
                () -> dataAccess.createAuth(auth)
        );
        assertEquals(auth.username(), dataAccess.getAuth("33").username());
        assertDoesNotThrow(
                () -> dataAccess.deleteAuth("33")
        );
        assertNull(dataAccess.getAuth("33"));
    }

    @Test
    void deleteAuthTwice() throws DataAccessException {
        AuthData auth = new AuthData("33", "ham");
        assertDoesNotThrow(
                () -> dataAccess.createAuth(auth)
        );
        assertEquals(auth.username(), dataAccess.getAuth("33").username());
        assertDoesNotThrow(
                () -> dataAccess.deleteAuth("33")
        );
        assertNull(dataAccess.getAuth("33"));
        assertNull(dataAccess.getAuth("33"));
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
