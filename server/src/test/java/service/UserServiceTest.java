package service;

import dataaccess.DAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.RegisterRequest;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private DAO dao;
    private UserService service;
    private ClearService clearer;

    @BeforeEach
    void setup() throws DataAccessException{
        dao = new MemoryDataAccess();
        service = new UserService(dao);
        clearer = new ClearService(dao);

        clearer.clearDB();
    }

    @Test
    void registerUser() throws DataAccessException{
        var user = new UserData("BagelBro", "Hotdog95", "BagelBroman@gmail.com");
        var result = service.register(new RegisterRequest(user.username(),user.password(), user.email()));

        assertNotNull(result);

        Collection<UserData> users = service.UserList();
        assertEquals(1, users.size());
        assertTrue(users.contains(user));
    }

    @Test
    void userAlreadyRegistered() throws DataAccessException{
        var user = new UserData("BagelBro", "Hotdog95", "BagelBroman@gmail.com");
        var user2 = new UserData("BagelBro", "72248&", "Davinky@gmail.com");
        service.register(new RegisterRequest(user.username(),user.password(), user.email()));
        Exception exception = assertThrows(DataAccessException.class,
                () -> service.register(new RegisterRequest(user2.username(),user2.password(), user2.email())));

        assertEquals("AlreadyTakenException", exception.getMessage());

        Collection<UserData> users = service.UserList();
        assertEquals(1, users.size());
        assertTrue(users.contains(user));
        assertFalse(users.contains(user2));
    }
}
