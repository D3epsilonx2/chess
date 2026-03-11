package server;

import dataaccess.DataAccessException;

public class ServerMain {
    static void main() throws DataAccessException {
        try {
            Server server = new Server();
            server.run(8080);
            System.out.println("♕ 240 Chess Server");
        } catch (Exception e){
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
    }
}