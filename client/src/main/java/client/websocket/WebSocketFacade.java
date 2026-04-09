package client.websocket;

import com.google.gson.Gson;
import websocket.messages.ServerMessage;
import websocket.commands.UserGameCommand;
import chess.ChessMove;

import jakarta.websocket.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;

    public WebSocketFacade(String url) {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
//                    notificationHandler.notify(serverMessage);
                }
            });

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    public void connect(String authToken, int gameID) throws Exception{
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws Exception {
        try{
            var command = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID);

            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void leaveGame(String authToken, int gameID) throws Exception {
        try{
            var command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void resign(String authToken, int gameID) throws Exception {
        try{
            var command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }
}
