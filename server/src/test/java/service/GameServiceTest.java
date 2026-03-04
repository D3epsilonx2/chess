package service;

import chess.ChessGame;
import dataaccess.DAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    private DAO dao;
    private GameService service;
    private UserService userserv;
    private ClearService clearer;

    @BeforeEach
    void setup() throws DataAccessException {
        dao = new MemoryDataAccess();
        service = new GameService(dao);
        userserv = new UserService(dao);
        clearer = new ClearService(dao);

        clearer.clearDB();
    }

    @Test
    public void createGameTest() throws DataAccessException{
        RegisterResult userResult = userserv.register(new RegisterRequest("BagelBro", "Hotdog95", "BagelBroman@gmail.com"));
        CreateGameRequest newcreatereq = new CreateGameRequest(userResult.authToken(), "TheGameOfChessYeah");

        assertDoesNotThrow(
                () -> service.createGame(newcreatereq)
        );

        GameListResult gameslistresult = service.getGamesList(userResult.authToken());
        assertEquals(1, gameslistresult.games().toArray().length);
    }

    @Test
    public void createNoName(){
        String authToken = UUID.randomUUID().toString();
        CreateGameRequest newcreatereq = new CreateGameRequest(authToken, null);

        Exception exception = assertThrows(DataAccessException.class,
                () -> service.createGame(newcreatereq)
        );
    }

    @Test
    public void gamesListOf3() throws DataAccessException {
        RegisterResult userResult = userserv.register(new RegisterRequest("BagelBro", "Hotdog95", "BagelBroman@gmail.com"));

        service.createGame(new CreateGameRequest(userResult.authToken(), "beef"));
        service.createGame(new CreateGameRequest(userResult.authToken(), "chicken"));
        service.createGame(new CreateGameRequest(userResult.authToken(), "pork"));

        GameListResult resultlist = service.getGamesList(userResult.authToken());
        assertEquals(3, resultlist.games().toArray().length);
    }

    @Test
    public void gamesListOf0() throws DataAccessException{
        RegisterResult userResult = userserv.register(new RegisterRequest("BagelBro", "Hotdog95", "BagelBroman@gmail.com"));

        GameListResult resultlist = service.getGamesList(userResult.authToken());
        assertEquals(0, resultlist.games().toArray().length);
    }

    @Test
    public void joinGameBasic() throws DataAccessException {
        RegisterResult regresult = userserv.register(new RegisterRequest("Frank", "1", "greg@gmail.com"));

        CreateGameResult createResult = service.createGame(new CreateGameRequest(regresult.authToken(), "chicken"));

        GameJoinRequest joinreq = new GameJoinRequest("WHITE", createResult.gameID(), regresult.authToken());
        assertDoesNotThrow(() -> service.joinGame(joinreq));

        GameListResult resultlist = service.getGamesList(regresult.authToken());
        GameData joinedGame = resultlist.games().iterator().next();
        assertEquals("Frank", joinedGame.whiteUsername());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameServiceTest that = (GameServiceTest) o;
        return Objects.equals(dao, that.dao) && Objects.equals(service, that.service) && Objects.equals(userserv, that.userserv) && Objects.equals(clearer, that.clearer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dao, service, userserv, clearer);
    }
}
