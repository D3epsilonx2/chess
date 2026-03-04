package service;

import dataaccess.DAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    private GameService service;
    private UserService userServ;

    @BeforeEach
    void setup() throws DataAccessException {
        DAO dao = new MemoryDataAccess();
        service = new GameService(dao);
        userServ = new UserService(dao);
        ClearService clearer = new ClearService(dao);

        clearer.clearDB();
    }

    @Test
    public void createGameTest() throws DataAccessException{
        RegisterResult userResult = userServ.register(new RegisterRequest("BagelBro", "Hotdog95", "BagelBroman@gmail.com"));
        CreateGameRequest newCreateReq = new CreateGameRequest(userResult.authToken(), "TheGameOfChessYeah");

        assertDoesNotThrow(
                () -> service.createGame(newCreateReq)
        );

        GameListResult gamesListResult = service.getGamesList(userResult.authToken());
        assertEquals(1, gamesListResult.games().toArray().length);
    }

    @Test
    public void createNoName() throws DataAccessException{
        RegisterResult userResult = userServ.register(new RegisterRequest("BagelBro", "Hotdog95", "BagelBroman@gmail.com"));
        CreateGameRequest newCreateReq = new CreateGameRequest(userResult.authToken(), null);

        Exception exception = assertThrows(DataAccessException.class,
                () -> service.createGame(newCreateReq)
        );
        assertEquals("BadEntryException", exception.getMessage());
    }

    @Test
    public void gamesListOf3() throws DataAccessException {
        RegisterResult userResult = userServ.register(new RegisterRequest("BagelBro", "Hotdog95", "BagelBroman@gmail.com"));

        service.createGame(new CreateGameRequest(userResult.authToken(), "beef"));
        service.createGame(new CreateGameRequest(userResult.authToken(), "chicken"));
        service.createGame(new CreateGameRequest(userResult.authToken(), "pork"));

        GameListResult resultList = service.getGamesList(userResult.authToken());
        assertEquals(3, resultList.games().toArray().length);
    }

    @Test
    public void gamesListOf0() throws DataAccessException{
        RegisterResult userResult = userServ.register(new RegisterRequest("BagelBro", "Hotdog95", "BagelBroman@gmail.com"));

        GameListResult resultList = service.getGamesList(userResult.authToken());
        assertEquals(0, resultList.games().toArray().length);
    }

    @Test
    public void gamesListBadAuth() throws DataAccessException{
        userServ.register(new RegisterRequest("BagelBro", "Hotdog95", "BagelBroman@gmail.com"));

        Exception exception = assertThrows(DataAccessException.class,
                () -> service.getGamesList("Forty")
        );
        assertEquals("AuthDNEException", exception.getMessage());

    }

    @Test
    public void joinGameBasic() throws DataAccessException {
        RegisterResult regResult = userServ.register(new RegisterRequest("Frank", "1", "greg@gmail.com"));

        CreateGameResult createResult = service.createGame(new CreateGameRequest(regResult.authToken(), "chicken"));

        GameJoinRequest joinReq = new GameJoinRequest("WHITE", createResult.gameID(), regResult.authToken());
        assertDoesNotThrow(() -> service.joinGame(joinReq));

        GameListResult resultList = service.getGamesList(regResult.authToken());
        GameData joinedGame = resultList.games().iterator().next();
        assertEquals("Frank", joinedGame.whiteUsername());
    }

    @Test
    public void joinGameTeamTaken() throws DataAccessException {
        RegisterResult userReg1 = userServ.register(new RegisterRequest("Jeff", "21", "greggy@gmail.com"));
        RegisterResult userReg2 = userServ.register(new RegisterRequest("Frank", "1", "greg@gmail.com"));

        CreateGameResult createResult = service.createGame(new CreateGameRequest(userReg2.authToken(), "chicken"));

        GameJoinRequest joinReq1 = new GameJoinRequest("WHITE", createResult.gameID(), userReg1.authToken());
        service.joinGame(joinReq1);

        GameJoinRequest joinReq2 = new GameJoinRequest("WHITE", createResult.gameID(), userReg2.authToken());
        Exception exception = assertThrows(DataAccessException.class,
                () -> service.joinGame(joinReq2)
        );
        assertEquals("TeamAlreadyTaken", exception.getMessage());

        GameListResult resultList = service.getGamesList(userReg2.authToken());
        GameData joinedGame = resultList.games().iterator().next();
        assertEquals("Jeff", joinedGame.whiteUsername());
    }
}
