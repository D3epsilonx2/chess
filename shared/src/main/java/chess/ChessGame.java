package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
    private ChessPosition BlackKingPos;
    private ChessPosition WhiteKingPos;


    public ChessGame() {
        BoardMain = new ChessBoard();
        BoardMain.resetBoard();
        TurnColor = TeamColor.WHITE;
        BlackKingPos = new ChessPosition(8, 5);
        WhiteKingPos = new ChessPosition(1, 5);
    }
//
//    public ChessGame(ChessGame Original){
//        this.BoardMain = new ChessBoard();
//        this.BoardMain.Copy(Original.BoardMain);
//
//        this.TurnColor = Original.TurnColor;
//
//        this.BlackKingPos = new ChessPosition(Original.BlackKingPos.getRow(), Original.BlackKingPos.getColumn());
//        this.WhiteKingPos = new ChessPosition(Original.WhiteKingPos.getRow(), Original.WhiteKingPos.getColumn());
//    }

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
        ChessPiece CurrPiece = BoardMain.getPiece(startPosition);
        Collection<ChessMove> AvailableMoves = CurrPiece.pieceMoves(BoardMain, startPosition);
        Collection<ChessMove> FinalMoves = new ArrayList<>();

        if (AvailableMoves == null){
            return null;
        }

        // Check moves using virtual board
        ChessBoard BoardClone = new ChessBoard();

        for (ChessMove move : AvailableMoves){
            if (move.getPromotionPiece() != null) {
                CurrPiece = new ChessPiece(TurnColor, move.getPromotionPiece());
            }
            BoardClone.Copy(BoardMain);
            BoardMain.addPiece(move.getEndPosition(),CurrPiece);
            BoardMain.addPiece(move.getStartPosition(), null);
            if (CurrPiece.getPieceType() == ChessPiece.PieceType.KING) {
                if (TurnColor == TeamColor.BLACK) {
                    BlackKingPos = move.getEndPosition();
                } else {
                    WhiteKingPos = move.getEndPosition();
                }
            }
            if (!isInCheck(TurnColor)){
                FinalMoves.add(move);
            }
            BoardMain.Copy(BoardClone);
            CurrPiece = BoardMain.getPiece(startPosition);
            if (CurrPiece.getPieceType() == ChessPiece.PieceType.KING) {
                if (TurnColor == TeamColor.BLACK) {
                    BlackKingPos = move.getEndPosition();
                } else {
                    WhiteKingPos = move.getEndPosition();
                }
            }
        }

        return FinalMoves;
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

        if (CurrPiece == null) {
            throw new InvalidMoveException("Piece is null");
        }

        Collection<ChessMove> ValidMoves = validMoves(move.getStartPosition());

        if (CurrPiece.getTeamColor() != TurnColor) {
            throw new InvalidMoveException("Piece out of turn");
        }
        if (!ValidMoves.contains(move)) {
            throw new InvalidMoveException("Invalid Move. Does not pass validity test.");
        }
//                    END OF VALIDITY CHECKS AND ERRORS
//                    PROMOTION, ELSE STANDARD MOVE
        if (move.getPromotionPiece() != null) {
            CurrPiece = new ChessPiece(TurnColor, move.getPromotionPiece());
        }
        BoardMain.addPiece(move.getEndPosition(), CurrPiece);
        BoardMain.addPiece(move.getStartPosition(), null);

//                        UPDATE KING POSITION FOR CHECK TRACKING
        if (CurrPiece.getPieceType() == ChessPiece.PieceType.KING) {
            if (TurnColor == TeamColor.BLACK) {
                BlackKingPos = move.getEndPosition();
            } else {
                WhiteKingPos = move.getEndPosition();
            }
        }
//                    TURN CHANGE
        if (TurnColor == TeamColor.BLACK) {
            TurnColor = TeamColor.WHITE;
        } else {
            TurnColor = TeamColor.BLACK;
        }

    }

    /**
     * verifies the kings location
     */
    public void KingVerify(TeamColor InColor){
        ChessPosition CheckPos;
        if (InColor == TeamColor.BLACK){
            CheckPos = BlackKingPos;
        }
        else{
            CheckPos = WhiteKingPos;
        }
        ChessPiece CheckPiece = BoardMain.getPiece(CheckPos);
//        if the king for that color isn't where it should be, find and reassign
        if ((CheckPiece == null) || (CheckPiece.getPieceType() != ChessPiece.PieceType.KING) || (CheckPiece.getTeamColor() != InColor)){
            for (int i = 1; i <= 8; i++){
                for (int j = 1; j <= 8; j++){
                    CheckPiece = BoardMain.getPiece(new ChessPosition(i, j));
                    if (CheckPiece == null){continue;}
                    if ((CheckPiece.getPieceType() == ChessPiece.PieceType.KING) && (CheckPiece.getTeamColor() == InColor)){
                        if (InColor == TeamColor.BLACK){
                            BlackKingPos = new ChessPosition(i, j);
                        }
                        else{
                            WhiteKingPos = new ChessPosition(i, j);
                        }
                    }
                }
            }
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        KingVerify(TeamColor.BLACK);
        KingVerify(TeamColor.WHITE);

        ChessPosition CurrPos;
        ChessPiece CheckPiece;
        if (teamColor == TeamColor.BLACK){
            CurrPos = BlackKingPos;
        }
        else{
            CurrPos = WhiteKingPos;
        }
//        ROOK QUEEN
//              NORTH
        for (int i = CurrPos.getRow()+1; i <= 8; i++){
            CheckPiece = BoardMain.getPiece(new ChessPosition(i, CurrPos.getColumn()));
            if(CheckPiece == null){
                continue;
            }
            if (CheckPiece.getTeamColor() == teamColor){
                break;
            }
            else{
                if((CheckPiece.getPieceType() == ChessPiece.PieceType.ROOK) || (CheckPiece.getPieceType() == ChessPiece.PieceType.QUEEN)){
                    return Boolean.TRUE;
                }
            }
        }
//              SOUTH
        for (int i = CurrPos.getRow()-1; i >= 1; i--){
            CheckPiece = BoardMain.getPiece(new ChessPosition(i, CurrPos.getColumn()));
            if(CheckPiece == null){
                continue;
            }
            if (CheckPiece.getTeamColor() == teamColor){
                break;
            }
            else{
                if((CheckPiece.getPieceType() == ChessPiece.PieceType.ROOK) || (CheckPiece.getPieceType() == ChessPiece.PieceType.QUEEN)){
                    return Boolean.TRUE;
                }
            }
        }
//              EAST
        for (int i = CurrPos.getColumn()+1; i <= 8; i++){
            CheckPiece = BoardMain.getPiece(new ChessPosition(CurrPos.getRow(), i));
            if(CheckPiece == null){
                continue;
            }
            if (CheckPiece.getTeamColor() == teamColor){
                break;
            }
            else{
                if((CheckPiece.getPieceType() == ChessPiece.PieceType.ROOK) || (CheckPiece.getPieceType() == ChessPiece.PieceType.QUEEN)){
                    return Boolean.TRUE;
                }
            }
        }
//              WEST
        for (int i = CurrPos.getColumn()-1; i >= 1; i--){
            CheckPiece = BoardMain.getPiece(new ChessPosition(CurrPos.getRow(), i));
            if(CheckPiece == null){
                continue;
            }
            if (CheckPiece.getTeamColor() == teamColor){
                break;
            }
            else{
                if((CheckPiece.getPieceType() == ChessPiece.PieceType.ROOK) || (CheckPiece.getPieceType() == ChessPiece.PieceType.QUEEN)){
                    return Boolean.TRUE;
                }
            }
        }
//        BISHOP QUEEN
//              NORTHEAST
        for (int i = 1; i <= 8; i++){
            if ((CurrPos.getRow()+i <= 8) && (CurrPos.getColumn()+i <= 8)) {
                CheckPiece = BoardMain.getPiece(new ChessPosition(CurrPos.getRow() + i, CurrPos.getColumn() + i));
                if (CheckPiece == null) {
                    continue;
                }
                if (CheckPiece.getTeamColor() == teamColor) {
                    break;
                } else {
                    if ((CheckPiece.getPieceType() == ChessPiece.PieceType.BISHOP) || (CheckPiece.getPieceType() == ChessPiece.PieceType.QUEEN)) {
                        return Boolean.TRUE;
                    }
                }
            }
            else{break;}
        }
//              NORTHWEST
        for (int i = 1; i <= 8; i++){
            if ((CurrPos.getRow()+i <= 8) && (CurrPos.getColumn()-i >= 1)) {
                CheckPiece = BoardMain.getPiece(new ChessPosition(CurrPos.getRow() + i, CurrPos.getColumn() - i));
                if (CheckPiece == null) {
                    continue;
                }
                if (CheckPiece.getTeamColor() == teamColor) {
                    break;
                } else {
                    if ((CheckPiece.getPieceType() == ChessPiece.PieceType.BISHOP) || (CheckPiece.getPieceType() == ChessPiece.PieceType.QUEEN)) {
                        return Boolean.TRUE;
                    }
                }
            }
            else{break;}
        }
//              SOUTHEAST
        for (int i = 1; i <= 8; i++){
            if ((CurrPos.getRow()-i >= 1) && (CurrPos.getColumn()+i <= 8)) {
                CheckPiece = BoardMain.getPiece(new ChessPosition(CurrPos.getRow() - i, CurrPos.getColumn() + i));
                if (CheckPiece == null) {
                    continue;
                }
                if (CheckPiece.getTeamColor() == teamColor) {
                    break;
                } else {
                    if ((CheckPiece.getPieceType() == ChessPiece.PieceType.BISHOP) || (CheckPiece.getPieceType() == ChessPiece.PieceType.QUEEN)) {
                        return Boolean.TRUE;
                    }
                }
            }
            else{break;}
        }
//              SOUTHWEST
        for (int i = 1; i <= 8; i++){
            if ((CurrPos.getRow()-i >= 1) && (CurrPos.getColumn()-i >= 1)) {
                CheckPiece = BoardMain.getPiece(new ChessPosition(CurrPos.getRow() - i, CurrPos.getColumn() - i));
                if (CheckPiece == null) {
                    continue;
                }
                if (CheckPiece.getTeamColor() == teamColor) {
                    break;
                } else {
                    if ((CheckPiece.getPieceType() == ChessPiece.PieceType.BISHOP) || (CheckPiece.getPieceType() == ChessPiece.PieceType.QUEEN)) {
                        return Boolean.TRUE;
                    }
                }
            }
            else{break;}
        }
//        KNIGHT
        List<ChessPosition> CheckingList = new ArrayList<>();

        CheckingList.add(new ChessPosition(CurrPos.getRow()+1, CurrPos.getColumn()+2));
        CheckingList.add(new ChessPosition(CurrPos.getRow()+2, CurrPos.getColumn()+1));

        CheckingList.add(new ChessPosition(CurrPos.getRow()-1, CurrPos.getColumn()+2));
        CheckingList.add(new ChessPosition(CurrPos.getRow()-2, CurrPos.getColumn()+1));

        CheckingList.add(new ChessPosition(CurrPos.getRow()+1, CurrPos.getColumn()-2));
        CheckingList.add(new ChessPosition(CurrPos.getRow()+2, CurrPos.getColumn()-1));

        CheckingList.add(new ChessPosition(CurrPos.getRow()-1, CurrPos.getColumn()-2));
        CheckingList.add(new ChessPosition(CurrPos.getRow()-2, CurrPos.getColumn()-1));

        for (ChessPosition CheckPos:CheckingList){
            if ((CheckPos.getRow() > 8) || (CheckPos.getRow() < 1) || (CheckPos.getColumn() > 8) || (CheckPos.getColumn() < 1)){
                continue;
            }
            CheckPiece = BoardMain.getPiece(CheckPos);
            if(CheckPiece == null){
                continue;
            }
            if((CheckPiece.getTeamColor() != teamColor) && (CheckPiece.getPieceType() == ChessPiece.PieceType.KNIGHT)){
                return Boolean.TRUE;
            }
        }
        CheckingList.clear();
//        PAWN
        if (CurrPos.equals(BlackKingPos)){
            if (CurrPos.getRow() <=7) {
                if (CurrPos.getColumn() <= 7) {
                    CheckPiece = BoardMain.getPiece(new ChessPosition(CurrPos.getRow() + 1, CurrPos.getColumn() + 1));
                    if ((CheckPiece != null) && (CheckPiece.getPieceType() == ChessPiece.PieceType.PAWN) && (CheckPiece.getTeamColor() == TeamColor.WHITE)) {
                        return Boolean.TRUE;
                    }
                }
                if (CurrPos.getColumn() >= 2) {
                    CheckPiece = BoardMain.getPiece(new ChessPosition(CurrPos.getRow() + 1, CurrPos.getColumn() - 1));
                    if ((CheckPiece != null) && (CheckPiece.getPieceType() == ChessPiece.PieceType.PAWN) && (CheckPiece.getTeamColor() == TeamColor.WHITE)) {
                        return Boolean.TRUE;
                    }
                }
            }
        }
        else {
            if (CurrPos.getRow() >= 2) {
                if (CurrPos.getColumn() <= 7) {
                    CheckPiece = BoardMain.getPiece(new ChessPosition(CurrPos.getRow() - 1, CurrPos.getColumn() + 1));
                    if ((CheckPiece != null) && (CheckPiece.getPieceType() == ChessPiece.PieceType.PAWN) && (CheckPiece.getTeamColor() == TeamColor.BLACK)) {
                        return Boolean.TRUE;
                    }
                }
                if (CurrPos.getColumn() >= 2) {
                    CheckPiece = BoardMain.getPiece(new ChessPosition(CurrPos.getRow() - 1, CurrPos.getColumn() - 1));
                    if ((CheckPiece != null) && (CheckPiece.getPieceType() == ChessPiece.PieceType.PAWN) && (CheckPiece.getTeamColor() == TeamColor.BLACK)) {
                        return Boolean.TRUE;
                    }
                }
            }
        }
//        KING
        CheckingList.add(new ChessPosition(CurrPos.getRow()+1, CurrPos.getColumn()+1));
        CheckingList.add(new ChessPosition(CurrPos.getRow()+1, CurrPos.getColumn()));
        CheckingList.add(new ChessPosition(CurrPos.getRow()+1, CurrPos.getColumn()-1));

        CheckingList.add(new ChessPosition(CurrPos.getRow()-1, CurrPos.getColumn()+1));
        CheckingList.add(new ChessPosition(CurrPos.getRow()-1, CurrPos.getColumn()));
        CheckingList.add(new ChessPosition(CurrPos.getRow()-1, CurrPos.getColumn()-1));

        CheckingList.add(new ChessPosition(CurrPos.getRow(), CurrPos.getColumn()+1));
        CheckingList.add(new ChessPosition(CurrPos.getRow(), CurrPos.getColumn()-1));
        for (ChessPosition CheckPos:CheckingList){
            if ((CheckPos.getRow() > 8) || (CheckPos.getRow() < 1) || (CheckPos.getColumn() > 8) || (CheckPos.getColumn() < 1)){
                continue;
            }
            CheckPiece = BoardMain.getPiece(CheckPos);
            if(CheckPiece == null){
                continue;
            }

            if((CheckPiece.getPieceType() == ChessPiece.PieceType.KING) && (CheckPiece.getTeamColor() != teamColor)){
                return Boolean.TRUE;
            }
        }
        CheckingList.clear();

        return Boolean.FALSE;
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
