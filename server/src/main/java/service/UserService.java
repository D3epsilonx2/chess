package service;

import dataaccess.DAO;
import dataaccess.DataAccessException;
import kotlin.NotImplementedError;
import model.*;

import java.util.UUID;

public class UserService {

    private final DAO dao;
    public UserService(DAO dao) {
        this.dao = dao;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException{
        try {
            if (dao.getUser(registerRequest.username()) == null){
                UserData user = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
                dao.createUser(user);

                String authToken = UUID.randomUUID().toString();
                AuthData auth = new AuthData(authToken, registerRequest.username());
                dao.createAuth(auth);

                return new RegisterResult(registerRequest.username(), authToken);
            }
            else{
                throw new DataAccessException("AlreadyTakenException");
            }
        } catch(DataAccessException ConnectionError) {
            throw new DataAccessException("Connection Error: 500");
        }
    }
    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        throw new NotImplementedError("Error: Not Implemented");
    }
    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
        throw new NotImplementedError("Error: Not Implemented");
    }
}
