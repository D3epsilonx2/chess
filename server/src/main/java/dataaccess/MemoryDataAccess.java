package dataaccess;

import chess.ChessGame;
import model.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class MemoryDataAccess implements DAO{
    final private HashMap<String, UserData> users = new HashMap<>();
    final private HashMap<String, AuthData> auths = new HashMap<>();
    final private HashMap<Integer, GameData> games = new HashMap<>();
    private int nextGameID = 1;

//    USERS
    public void createUser(UserData user) throws DataAccessException{
        if (users.containsKey(user.username())){
            throw new DataAccessException("AlreadyTakenException");
        }
        users.put(user.username(), user);
    }

    public UserData getUser(String username){
        return users.get(username);
    }

    public Collection<UserData> listUsers(){
        return users.values();
    }

//    UserData updateUser(UserData user)

    public void deleteUser(String username){
        users.remove(username);
    }

    public void deleteAllUsers(){
        users.clear();
    }

//    GAMEDAO

    public int createGame(GameData game){
        int id = nextGameID;
        nextGameID++;
        GameData newgame = new GameData(id, game.whiteUsername(), game.blackUsername(), game.gameName(), new ChessGame());
        games.put(id, newgame);
        return id;
    }

    public GameData getGame(int gameID){
        return games.get(gameID);
    }

    public Collection<GameData> listGames(){
        return games.values();
    }

    public void updateGame(GameData game){
        games.put(game.gameID(), game);
    }

    public void deleteGame(int gameID){
        games.remove(gameID);
    }

    public void deleteAllGames(){
        games.clear();
    }

//    AUTHDAO

    public void createAuth(AuthData auth){
        auths.put(auth.authToken(), auth);
    }

    public AuthData getAuth(String authToken){
        return auths.get(authToken);
    }

//    AuthData updateAuth(AuthData auth)

    public void deleteAuth(String authToken){
        auths.remove(authToken);
    }

    public void deleteAllAuths(){
        auths.clear();
    }

    public void clear(){
        users.clear();
        auths.clear();
        games.clear();
        nextGameID = 1;
    }
}
