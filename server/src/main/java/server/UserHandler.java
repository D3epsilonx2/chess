package server;

import com.google.gson.Gson;
import dataaccess.DAO;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.LoginRequest;
import model.LogoutRequest;
import model.RegisterRequest;
import service.UserService;

import java.util.Map;

public class UserHandler {
    private final UserService userService;

    public UserHandler(DAO dao){
        this.userService = new UserService(dao);
    }

    public void register(Context context){
        RegisterRequest regreq = new Gson().fromJson(context.body(), RegisterRequest.class);

        if(regreq.username() == null || regreq.password() == null || regreq.email() == null){
            context.contentType("application/json");
            context.status(400);
            context.result(new Gson().toJson(Map.of("message", "Error: Bad Request")));
        } else {

            try {
                var result = userService.register(regreq);

                context.contentType("application/json");
                context.result(new Gson().toJson(result));
            } catch (DataAccessException exception) {
                context.contentType("application/json");
                if (exception.getMessage().contains("Already")) {
                    context.status(403);
                    context.result(new Gson().toJson(Map.of("message", "Error: Already Taken")));
                } else {
                    context.status(500);
                    context.result(new Gson().toJson(Map.of("message", "Error: Unknown connection error")));
                }
            }
        }
    }

    public void login(Context context){
        LoginRequest inreq = new Gson().fromJson(context.body(), LoginRequest.class);

        if(inreq.username() == null || inreq.password() == null){
            context.contentType("application/json");
            context.status(400);
            context.result(new Gson().toJson(Map.of("message", "Error: Bad Request")));
        } else {

            try {
                var result = userService.login(inreq);

                context.contentType("application/json");
                context.result(new Gson().toJson(result));
            } catch (DataAccessException exception) {
                context.contentType("application/json");
                if (exception.getMessage().contains("User")) {
                    context.status(401);
                    context.result(new Gson().toJson(Map.of("message", "Error: User Does Not Exist")));
                } else if (exception.getMessage().contains("Wrong")) {
                    context.status(401);
                    context.result(new Gson().toJson(Map.of("message", "Error: Wrong Password")));
                } else {
                    context.status(500);
                    context.result(new Gson().toJson(Map.of("message", "Error: Unknown connection error")));
                }
            }
        }
    }

    public void logout(Context context){
        LogoutRequest outreq = new LogoutRequest(context.header("authorization"));

        try{
            userService.logout(outreq);
            context.contentType("application/json");
            context.status(200);
            context.result("{}");
        } catch (DataAccessException exception){
            context.contentType("application/json");
            if (exception.getMessage().contains("AuthDNE")){
                context.status(401);
                context.result(new Gson().toJson(Map.of("message", "Error: Unauthorized")));
            } else{
                context.status(500);
                context.result(new Gson().toJson(Map.of("message", "Error: Unknown connection error")));
            }
        }
    }
}
