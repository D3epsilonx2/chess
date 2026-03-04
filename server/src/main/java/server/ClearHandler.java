package server;

import com.google.gson.Gson;
import dataaccess.DAO;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.ClearService;

import java.util.Map;

public class ClearHandler {

    private final ClearService clearService;

    public ClearHandler(DAO dao){
        this.clearService = new ClearService(dao);
    }

    public void clear(Context context){
        try{
            clearService.clearDB();
            context.contentType("application/json");
            context.result("{}");
        } catch (DataAccessException exception){
            context.status(500);
            context.result(new Gson().toJson(Map.of("message", "Error: Unknown Connection Error")));
        }
    }

}
