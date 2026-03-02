package service;

import chess.ChessGame;
import dataaccess.DAO;
import dataaccess.DataAccessException;
import kotlin.NotImplementedError;
import model.AuthData;
import model.GameData;

import java.util.Objects;

public class GameService {

    private final DAO dao;

    public GameService(DAO dao) { this.dao = dao;}

    public gameListResult getGamesList(String authToken) throws DataAccessException {
        try {
            AuthData auth = dao.getAuth(authToken);

            dao.listGames();
        } catch() {
//            AUTHDNEEXCEPTION
        }
        throw new NotImplementedError("Error: Not Implemented");
    }
    public createGameResult createGame(String authToken, String gameName) throws DataAccessException{
        try {
            AuthData auth = dao.getAuth(authToken);

            GameData game = new GameData(0, "", "", gameName, new ChessGame());
            dao.createGame(game);
        } catch() {
//            AUTHDNEEXCEPTION
        }
        throw new NotImplementedError("Error: Not Implemented");
    }
    public void joinGame(String playerColor, String gameID, String authToken) throws DataAccessException{
        try {
            AuthData auth = dao.getAuth(authToken);

            try {
                GameData game = dao.getGame(gameID);

                if (Objects.equals(playerColor, "BLACK")) {
                    GameData updatedGame = new GameData(game.gameID(), game.whiteUsername(), auth.username(), game.gameName(), game.game());
                    dao.updateGame(updatedGame);
                }
                else {
                    GameData updatedGame = new GameData(game.gameID(), auth.username(), game.blackUsername(), game.gameName(), game.game());
                    dao.updateGame(updatedGame);
                }

            } catch () {
//                GAMEDNEEXCEPTION
            }
        } catch() {
//            AUTHDNEEXCEPTION
        }
        throw new NotImplementedError("Error: Not Implemented");
    }
}
