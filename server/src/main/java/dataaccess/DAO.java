package dataaccess;

import model.*;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Create, Read, Update, Delete
 */
public interface DAO {
//    USERDAO

    void createUser(UserData user) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    Collection<UserData> listUsers() throws DataAccessException;

//    UserData updateUser(UserData user) throws DataAccessException;

    void deleteUser(String username) throws DataAccessException;

    void deleteAllUsers() throws DataAccessException;

//    GAMEDAO

    int createGame(GameData game) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    void updateGame(GameData game) throws DataAccessException;

    void deleteGame(int gameID) throws DataAccessException;

    void deleteAllGames() throws DataAccessException;

//    AUTHDAO

    void createAuth(AuthData auth) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

//    AuthData updateAuth(AuthData auth) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;

    void deleteAllAuths() throws DataAccessException;


    void clear() throws  DataAccessException;
}
