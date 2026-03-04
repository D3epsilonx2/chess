package service;

import chess.ChessGame;
import dataaccess.DAO;
import dataaccess.DataAccessException;
import kotlin.NotImplementedError;
import model.AuthData;
import model.GameData;
import model.createGameResult;
import model.gameListResult;

import java.util.Objects;

public class GameService {

    private final DAO dao;

    public GameService(DAO dao) { this.dao = dao;}

    public gameListResult getGamesList(String authToken) throws DataAccessException {
        var auth = dao.getAuth(authToken);
        if (auth == null){
            throw new DataAccessException("AuthDNEException");
        }
        return new gameListResult(dao.listGames());
    }
    public createGameResult createGame(String authToken, String gameName) throws DataAccessException{
        var auth = dao.getAuth(authToken);
        if (auth == null){
            throw new DataAccessException("AuthDNEException");
        }
        GameData game = new GameData(0, "", "", gameName, new ChessGame());
        dao.createGame(game);
        return new createGameResult(game.gameID());
    }
    public void joinGame(String playerColor, int gameID, String authToken) throws DataAccessException {

        var auth = dao.getAuth(authToken);
        if (auth == null){
            throw new DataAccessException("AuthDNEException");
        }

        GameData game = dao.getGame(gameID);

        if (Objects.equals(playerColor, "BLACK")) {
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
