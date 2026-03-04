package service;

import dataaccess.DAO;
import dataaccess.DataAccessException;

public class ClearService {

    private final DAO dao;

    public ClearService(DAO dao) { this.dao = dao;}

    public void clearDB() throws DataAccessException {
        dao.clear();
    }
}
