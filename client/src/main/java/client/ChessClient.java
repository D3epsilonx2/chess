package client;

import model.*;
import server.ServerFacade;
import ui.State;
import ui.EscapeSequences;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ChessClient {
    private final ServerFacade server;
    private String currUsername = null;
    private String currAuth = null;
    private List<GameData> gameList = new java.util.ArrayList<>();
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
            if (state == State.SIGNEDOUT) {
                return switch (cmd) {
                    case "quit" -> "quit";
                    case "login" -> login(params);
                    case "register" -> register(params);
                    default -> help();
                };
            } else {
                return switch (cmd) {
                    case "quit" -> "quit";
                    case "logout" -> signOut();
                    case "creategame" -> createGame(params);
                    case "listgames" -> listGames();
                    case "playgame" -> joinGame(params);
                    case "observegame" -> observeGame(params);
                    default -> help();
                };
            }
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
        AuthData auth = server.register(params[0], params[1], params[2]);
        state = State.SIGNEDIN;
        currUsername = auth.username();
        currAuth = auth.authToken();
//          FIGURE OUT THIS BIT
        return String.format("Welcome, %s.", currUsername);

    }

    public String signOut() throws Exception {
        assertSignedIn();
        server.logout(currAuth);
        state = State.SIGNEDOUT;
        String tempName = currUsername;
        currUsername = null;
        currAuth = null;
        return String.format("You have successfully signed out, %s.", tempName);
    }

    public String createGame(String... params) throws Exception {
        assertSignedIn();

        if (params.length != 1){
            throw new Exception("Error: expected 2 arguments but got" + params.length);
        }

        server.createGame(currAuth, params[0]);
        return String.format("Created game %s.", params[0]);
    }

    public String listGames() throws Exception {
        assertSignedIn();
        GameListResult games = server.listGames(currAuth);
        gameList = new java.util.ArrayList<>(games.games());
        var result = new StringBuilder();
        for (int i = 0; i < gameList.size(); i++){
            GameData game = gameList.get(i);
            result.append(String.format("%d. %s | White: %s | Black: %s%n",
                    i + 1,
                    game.gameName(),
                    game.whiteUsername() != null ? game.whiteUsername() : "open",
                    game.blackUsername() != null ? game.blackUsername() : "open"));
        }
        return result.toString();
    }

    public String joinGame(String... params) throws Exception {
        assertSignedIn();

        if (gameList.isEmpty()) {
            throw new Exception("Please run 'listgames' first.");
        }

        if (params.length < 2){
            throw new Exception("Error: expected 2 arguments but got" + params.length);
        }

        int index = Integer.parseInt(params[0]) - 1;
        if (index < 0 || index >= gameList.size()){
            throw new Exception("Invalid game number");
        }

        int gameID = gameList.get(index).gameID();
        String color = (params[1] != null) ? params[1].toUpperCase() : null;
        server.joinGame(currAuth, color, gameID);
        return String.format("Successfully joined game: %s", params[1]);
    }

    public String observeGame(String... params) throws Exception {
        assertSignedIn();

        if (gameList.isEmpty()) {
            throw new Exception("Please run 'listgames' first.");
        }

        if (params.length != 1){
            throw new Exception("Error: one parameter expected");
        }
        return joinGame(params[0], null);
    }

    public String help() {
        if (state == State.SIGNEDIN){
            return """
                    - Logout
                    - CreateGame <game Name>
                    - ListGames
                    - JoinGame <color> <game number>
                    - ObserveGame <game number>
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
