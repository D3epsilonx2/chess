package service;

import dataaccess.DAO;
import dataaccess.DataAccessException;
import model.*;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class UserService {

    private final DAO dao;
    public UserService(DAO dao) {
        this.dao = dao;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException{
        if (dao.getUser(registerRequest.username()) != null){
            throw new DataAccessException("AlreadyTakenException");
        }

        UserData user = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        dao.createUser(user);

        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, registerRequest.username());
        dao.createAuth(auth);

        return new RegisterResult(registerRequest.username(), authToken);
    }

    public Collection<UserData> userList() throws DataAccessException{
        try{
            return dao.listUsers();
        } catch(DataAccessException ConnectionError) {
            throw new DataAccessException("Connection Error: 500");
        }
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        var user = dao.getUser(loginRequest.username());
        if (user == null){
            throw new DataAccessException("UserDNEException");
        }
        if (!Objects.equals(user.password(), loginRequest.password())){
            throw new DataAccessException("WrongPasswordException");
        }
        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, loginRequest.username());
        dao.createAuth(auth);

        return new LoginResult(loginRequest.username(), authToken);
    }
    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
        var authdat = dao.getAuth(logoutRequest.authToken());
        if (authdat == null){
            throw new DataAccessException("AuthDNEException");
        }
        dao.deleteAuth(logoutRequest.authToken());
    }
}
