package service;

import dataaccess.DAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {
    private DAO dao;
    private GameService gameserv;
    private UserService userserv;
    private ClearService clearserv;

    @BeforeEach
    void setup() throws DataAccessException {
        dao = new MemoryDataAccess();
        gameserv = new GameService(dao);
        userserv = new UserService(dao);
        clearserv = new ClearService(dao);

        clearserv.clearDB();
    }
    @Test
    public void gamesListOf3clear() throws DataAccessException {
        RegisterResult userResult = userserv.register(new RegisterRequest("BagelBro", "Hotdog95", "BagelBroman@gmail.com"));

        gameserv.createGame(new CreateGameRequest(userResult.authToken(), "beef"));
        gameserv.createGame(new CreateGameRequest(userResult.authToken(), "chicken"));
        gameserv.createGame(new CreateGameRequest(userResult.authToken(), "pork"));

        GameListResult resultlist = gameserv.getGamesList(userResult.authToken());
        assertEquals(3, resultlist.games().toArray().length);

        clearserv.clearDB();
        RegisterResult userAgain = userserv.register(new RegisterRequest("BagelBro", "Hotdog95", "BagelBroman@gmail.com"));
        GameListResult resultagain = gameserv.getGamesList(userAgain.authToken());
        assertEquals(0, resultagain.games().toArray().length);
    }

    @Test
    void userLoginFail() throws DataAccessException{
        var user = new UserData("BagelBro", "Hotdog95", "BagelBroman@gmail.com");
        userserv.register(new RegisterRequest(user.username(),user.password(), user.email()));

        Collection<UserData> users = userserv.UserList();
        assertEquals(1, users.size());
        assertTrue(users.contains(user));

        clearserv.clearDB();

        Exception exception = assertThrows(DataAccessException.class,
                () -> userserv.login(new LoginRequest(user.username(), user.password()))
        );

        assertEquals("UserDNEException", exception.getMessage());
    }
}
