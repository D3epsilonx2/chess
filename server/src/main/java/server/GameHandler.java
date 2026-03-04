package server;

import com.google.gson.Gson;
import dataaccess.DAO;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.CreateGameRequest;
import model.GameJoinRequest;
import model.GameListResult;
import model.RegisterRequest;
import service.GameService;

import java.util.Map;

public class GameHandler {
    private final GameService gameService;

    public GameHandler(DAO dao){
        this.gameService = new GameService(dao);
    }

    public void getGamesList(Context context){
        String authToken = context.header("authorization");

        try{
            var result = gameService.getGamesList(authToken);

            context.contentType("application/json");
            context.result(new Gson().toJson(result));
        } catch(DataAccessException exception){
            context.contentType("application/json");
            if (exception.getMessage().contains("AuthDNE")){
                context.status(401);
                context.result(new Gson().toJson(Map.of("message", "Error: Unauthorized")));
            } else{
                context.status(500);
                context.result(new Gson().toJson(Map.of("message", "Error: Unknown Connection Error")));
            }
        }
    }

    public void createGame(Context context){
        String authToken = context.header("authorization");

        if (context.body().isEmpty() || !context.body().contains("gameName")){
            context.contentType("application/json");
            context.status(400);
            context.result(new Gson().toJson(Map.of("message", "Error: Bad Request")));
        }

        try{
            CreateGameRequest createreq = new Gson().fromJson(context.body(), CreateGameRequest.class);
            createreq = new CreateGameRequest(authToken, createreq.gameName());

            var result = gameService.createGame(createreq);

            context.contentType("application/json");
            context.result(new Gson().toJson(result));
        } catch(DataAccessException exception){
            context.contentType("application/json");
            if (exception.getMessage().contains("AuthDNE")){
                context.status(401);
                context.result(new Gson().toJson(Map.of("message", "Error: Unauthorized")));
            } else{
                context.status(500);
                context.result(new Gson().toJson(Map.of("message", "Error: Unknown Connection Error")));
            }
        }
    }

    public void joinGame(Context context){
        String authToken = context.header("authorization");

        if (context.body().isEmpty() || !context.body().contains("gameName")){
            context.contentType("application/json");
            context.status(400);
            context.result(new Gson().toJson(Map.of("message", "Error: Bad Request")));
        }

        try{
            GameJoinRequest joinreq = new Gson().fromJson(context.body(), GameJoinRequest.class);
            joinreq = new GameJoinRequest(joinreq.playerColor(), joinreq.gameID(), authToken);

            gameService.joinGame(joinreq);

            context.contentType("application/json");
            context.status(200);
            context.result("{}");
        } catch(DataAccessException exception){
            context.contentType("application/json");
            if (exception.getMessage().contains("AuthDNE")){
                context.status(401);
                context.result(new Gson().toJson(Map.of("message", "Error: Unauthorized")));
            } else if (exception.getMessage().contains("Team")) {
                context.status(403);
                context.result(new Gson().toJson(Map.of("message", "Error: Team Already Taken")));
            } else{
                context.status(500);
                context.result(new Gson().toJson(Map.of("message", "Error: Unknown Connection Error")));
            }
        }
    }
}
