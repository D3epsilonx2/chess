package service;

import chess.ChessGame;
import dataaccess.DAO;
import dataaccess.DataAccessException;
import model.*;

import java.util.Objects;

public class GameService {

    private final DAO dao;

    public GameService(DAO dao) { this.dao = dao;}

    public GameListResult getGamesList(String authToken) throws DataAccessException {
        var auth = dao.getAuth(authToken);
        if (auth == null){
            throw new DataAccessException("AuthDNEException");
        }
        return new GameListResult(dao.listGames());
    }
    public CreateGameResult createGame(CreateGameRequest createRequest) throws DataAccessException{
        var auth = dao.getAuth(createRequest.authToken());
        if (auth == null){
            throw new DataAccessException("AuthDNEException");
        }
        GameData game = new GameData(0, null, null, createRequest.gameName(), new ChessGame());
        int gameID = dao.createGame(game);
        return new CreateGameResult(gameID);
    }
    public void joinGame(GameJoinRequest joinRequest) throws DataAccessException {

        var auth = dao.getAuth(joinRequest.authToken());
        if (auth == null){
            throw new DataAccessException("AuthDNEException");
        }

        GameData game = dao.getGame(joinRequest.gameID());

        if (Objects.equals(joinRequest.playerColor(), "BLACK")) {
            if(game.blackUsername() != null){
                throw new DataAccessException("TeamAlreadyTaken");
            }
            GameData updatedGame = new GameData(game.gameID(), game.whiteUsername(), auth.username(), game.gameName(), game.game());
            dao.updateGame(updatedGame);
        } else {
            if(game.whiteUsername() != null){
                throw new DataAccessException("TeamAlreadyTaken");
            }
            GameData updatedGame = new GameData(game.gameID(), auth.username(), game.blackUsername(), game.gameName(), game.game());
            dao.updateGame(updatedGame);
        }
    }
}
