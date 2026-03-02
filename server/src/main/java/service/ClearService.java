package service;

import dataaccess.DAO;
import dataaccess.DataAccessException;
import kotlin.NotImplementedError;

public class ClearService {

    private final DAO dao;

    public ClearService(DAO dao) { this.dao = dao;}

    public void clearDB() throws DataAccessException {
        dao.deleteAllUsers();
        dao.deleteAllGames();
        dao.deleteAllAuths();
    }
}
