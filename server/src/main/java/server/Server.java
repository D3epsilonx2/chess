package server;

import dataaccess.DAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import dataaccess.MySqlDataAccess;
import io.javalin.*;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        DAO dao;
        try {
            dao = new MySqlDataAccess();
        } catch(DataAccessException e){
            dao = new MemoryDataAccess();
        }
        UserHandler userHandler = new UserHandler(dao);
        GameHandler gameHandler = new GameHandler(dao);
        ClearHandler clearHandler = new ClearHandler(dao);
        // Register your endpoints and exception handlers here.
        javalin.post("/user", userHandler::register);
        javalin.post("/session", userHandler::login);
        javalin.delete("/session", userHandler::logout);
        javalin.get("/game", gameHandler::getGamesList);
        javalin.post("/game", gameHandler::createGame);
        javalin.put("/game", gameHandler::joinGame);
        javalin.delete("/db", clearHandler::clear);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
