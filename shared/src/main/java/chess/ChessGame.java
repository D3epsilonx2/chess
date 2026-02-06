package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessGame.TeamColor TurnColor;
    private ChessBoard BoardMain;
//    private Boolean BlackInCheck;
//    private Boolean WhiteInCheck;
//    private ChessPosition BlackKingPos;
//    private ChessPosition WhiteKingPos;


    public ChessGame() {
        BoardMain = new ChessBoard();
        BoardMain.resetBoard();
        TurnColor = TeamColor.WHITE;
//        BlackInCheck = Boolean.FALSE;
//        WhiteInCheck = Boolean.FALSE;
//        BlackKingPos = new ChessPosition(8, 5);
//        WhiteKingPos = new ChessPosition(1, 5);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return TurnColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        TurnColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece CurrentPiece = BoardMain.getPiece(startPosition);
        Collection<ChessMove> AvailableMoves = CurrentPiece.pieceMoves(BoardMain, startPosition);
        Collection<ChessMove> FinalMoves = new ArrayList<>();

        if (AvailableMoves == null){
            return null;
        }

        // Check moves using makeMove method
        for (ChessMove move : AvailableMoves){
            ChessGame GameCopy = new ChessGame();
            GameCopy.setBoard(BoardMain);
            try {
                makeMove(move);
                FinalMoves.add(move);
            } catch (InvalidMoveException e) {
            }
        }

        if (!FinalMoves.isEmpty()) {
            return FinalMoves;
        }
        else{
            return null;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     * ex, a piece tries to move when the king is in check that isn't the king.
     * See boolean examples below
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece CurrPiece = BoardMain.getPiece(move.getStartPosition());

        if (CurrPiece != null){
            if (CurrPiece.getTeamColor() == TurnColor){
                BoardMain.addPiece(move.getEndPosition(), CurrPiece);
                BoardMain.addPiece(move.getStartPosition(), null);
            }
            else{
                throw new InvalidMoveException("Piece out of turn");
            }
        }
        else{
            throw new InvalidMoveException("Piece is null");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
//        Collection<ChessMove> EnemyMoves = new ArrayList<>();
//        Collection<ChessMove> myKingMoves = ;
//
//        for
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        BoardMain = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return BoardMain;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return TurnColor == chessGame.TurnColor && Objects.equals(BoardMain, chessGame.BoardMain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(TurnColor, BoardMain);
    }
}
