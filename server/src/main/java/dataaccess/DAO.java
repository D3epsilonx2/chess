package dataaccess;

import model.*;

import java.util.LinkedHashSet;

/**
 * Create, Read, Update, Delete
 */
public interface DAO {
//    USERDAO

    UserData createUser(UserData user) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

//    UserData updateUser(UserData user) throws DataAccessException;

    void deleteUser(String username) throws DataAccessException;

    void deleteAllUsers() throws DataAccessException;

//    GAMEDAO

    GameData createGame(GameData game) throws DataAccessException;

    GameData getGame(String gameToken) throws DataAccessException;

    LinkedHashSet<GameData> listGames() throws DataAccessException;

    void updateGame(GameData game) throws DataAccessException;

    void deleteGame(GameData game) throws DataAccessException;

    void deleteAllGames() throws DataAccessException;

//    AUTHDAO

    AuthData createAuth(AuthData auth) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

//    AuthData updateAuth(AuthData auth) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;

    void deleteAllAuths() throws DataAccessException;

}
