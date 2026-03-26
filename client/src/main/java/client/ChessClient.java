package client;

import com.google.gson.Gson;
import jdk.jshell.spi.ExecutionControl;
import model.*;
import server.ServerFacade;
import ui.State;
import ui.EscapeSequences;

import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {
    private String currUsername = null;
    private String currAuth = null;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl){
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN +
                "Welcome! Please log in to play Chess!");

        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + msg +
                        EscapeSequences.RESET_TEXT_COLOR);
            }
        }
        System.out.println(EscapeSequences.SET_BG_COLOR_GREEN +
                "Thank you for playing!" + EscapeSequences.RESET_TEXT_COLOR);
    }

    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.RESET_TEXT_COLOR +
                ">>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "Quit" -> "quit";
                case "Login" -> login(params);
                case "Register" -> register(params);
                case "Logout" -> signOut();
                case "CreateGame" -> createGame(params);
//                case "ListGames" -> listGames();
                case "PlayGame" -> playGame(params);
//                case "ObserveGame" -> observe(params);
                default -> help();
            };
//       do error here
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws Exception {
        if (params.length < 2){
            throw new Exception("Error: expected 3 arguments but got" + params.length);
        }
        AuthData auth = server.login(params[0], params[1]);
        state = State.SIGNEDIN;

        currUsername = auth.username();
        currAuth = auth.authToken();
//          FIGURE OUT THIS BIT
        return String.format("Welcome, %s.", currUsername);

    }

    public String register(String... params) throws Exception {
        if (params.length < 3){
            throw new Exception("Error: expected 3 arguments but got" + params.length);
        }
        AuthData auth = server.register(params[0], params[1], params[3]);
        state = State.SIGNEDIN;
        currUsername = auth.username();
        currAuth = auth.authToken();
//          FIGURE OUT THIS BIT
        return String.format("Welcome, %s.", currUsername);

    }

    public String signOut() throws Exception {
        assertSignedIn();
        state = State.SIGNEDOUT;
        return String.format("You have successfully signed out, %s.", currUsername);
    }

    public String createGame(String... params) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("not implemented");
    }

//    public String listGames() throws Exception {
//        assertSignedIn();
//        String games = server.listGames();
//        var result = new StringBuilder();
//        var gson = new Gson();
//        for (GameData game : games){
//            result.append(gson.toJson(game)).append('\n');
//        }
//        return result.toString();
//    }

    public String playGame(String... params) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("not implemented");
    }

    public String help() {
        if (state == State.SIGNEDIN){
            return """
                    - Logout
                    - CreateGame <game Name>
                    - ListGames
                    - PlayGame <game number> <color>
                    - ObserveGame
                    """;
        }
        return """
                    - Quit
                    - Login <username> <password> <email>
                    - Register <username> <password> <email>
                    """;
    }

    private void assertSignedIn() throws Exception{
        if (state == State.SIGNEDOUT){
            throw new Exception("You must be signed in");
        }
    }
}
