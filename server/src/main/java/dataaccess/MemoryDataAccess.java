package dataaccess;

import chess.ChessGame;
import model.*;

import java.util.Collection;
import java.util.HashMap;

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

    public UserData getUser(String username) {
        return users.get(username);
    }

    public Collection<UserData> listUsers() {
        return users.values();
    }

//    GAMEDAO

    public int createGame(GameData game) throws DataAccessException {
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

//    AUTHDAO

    public void createAuth(AuthData auth){
        auths.put(auth.authToken(), auth);
    }

    public AuthData getAuth(String authToken){
        return auths.get(authToken);
    }

    public void deleteAuth(String authToken){
        auths.remove(authToken);
    }

    public void clear(){
        users.clear();
        auths.clear();
        games.clear();
        nextGameID = 1;
    }
}
