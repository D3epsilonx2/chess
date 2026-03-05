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

    private ChessGame.TeamColor turnColor;
    private ChessBoard boardMain;
    private ChessPosition blackKingPos;
    private ChessPosition whiteKingPos;


    public ChessGame() {
        boardMain = new ChessBoard();
        boardMain.resetBoard();
        turnColor = TeamColor.WHITE;
        blackKingPos = new ChessPosition(8, 5);
        whiteKingPos = new ChessPosition(1, 5);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turnColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turnColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Officiates the move on the board
     * @param move the move to be made
     */
    public void moveUpdate(ChessMove move){
        ChessPiece currPiece = boardMain.getPiece(move.getStartPosition());
        if (move.getPromotionPiece() != null) {
            currPiece = new ChessPiece(currPiece.getTeamColor(), move.getPromotionPiece());
        }
        boardMain.addPiece(move.getEndPosition(),currPiece);
        boardMain.addPiece(move.getStartPosition(), null);
    }

//    HELPER METHODS

    /**
     * checks for threats in straight line
     * @param currPos current king position
     * @param teamColor current team color
     * @param rowStep row number to iterate
     * @param colStep column number to iterate
     * @return bool for true if there's a threat
     */

    private boolean straightLineChecker(ChessPosition currPos, TeamColor teamColor,
                                        int rowStep, int colStep) {
        int row = currPos.getRow() + rowStep;
        int col = currPos.getColumn() + colStep;
        while (row >= 1 && row <= 8 && col >= 1 && col <= 8) {
            ChessPiece checkPiece = boardMain.getPiece(new ChessPosition(row, col));
            if (checkPiece == null) {
                row += rowStep;
                col += colStep;
                continue;
            }
            if (checkPiece.getTeamColor() == teamColor) {
                break;
            }
            if (checkPiece.getPieceType() == ChessPiece.PieceType.ROOK
                    || checkPiece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                return true;
            }
            break;
        }
        return false;
    }

    /**
     * checks for threats in diagonal line
     * @param currPos current king position
     * @param teamColor current team color
     * @param rowStep row number to iterate
     * @param colStep column number to iterate
     * @return bool for true if there's a threat
     */

    private boolean diagonalLineChecker(ChessPosition currPos, TeamColor teamColor,
                                        int rowStep, int colStep) {
        int row = currPos.getRow() + rowStep;
        int col = currPos.getColumn() + colStep;
        while (row >= 1 && row <= 8 && col >= 1 && col <= 8) {
            ChessPiece checkPiece = boardMain.getPiece(new ChessPosition(row, col));
            if (checkPiece == null) {
                row += rowStep;
                col += colStep;
                continue;
            }
            if (checkPiece.getTeamColor() == teamColor) {
                break;
            }
            if (checkPiece.getPieceType() == ChessPiece.PieceType.BISHOP
                    || checkPiece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                return true;
            }
            break;
        }
        return false;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece currPiece = boardMain.getPiece(startPosition);
        if (currPiece == null){
            return null;
        }
        ChessPiece.PieceType currType = currPiece.getPieceType();
        TeamColor currColor = currPiece.getTeamColor();

        Collection<ChessMove> availableMoves = currPiece.pieceMoves(boardMain, startPosition);
        Collection<ChessMove> finalMoves = new ArrayList<>();

        if (availableMoves == null){
            return null;
        }

        // Check moves using virtual board
        ChessBoard boardClone = new ChessBoard();

        for (ChessMove move : availableMoves){
            kingVerify(TeamColor.BLACK);
            kingVerify(TeamColor.WHITE);

            boardClone.copier(boardMain);
            moveUpdate(move);
            if (currType == ChessPiece.PieceType.KING) {
                if (currColor == TeamColor.BLACK) {
                    blackKingPos = move.getEndPosition();
                } else {
                    whiteKingPos = move.getEndPosition();
                }
            }
            if (!isInCheck(currColor)){
                finalMoves.add(move);
            }
            boardMain.copier(boardClone);
            if (currType == ChessPiece.PieceType.KING) {
                if (currColor == TeamColor.BLACK) {
                    blackKingPos = move.getStartPosition();
                } else {
                    whiteKingPos = move.getStartPosition();
                }
            }
        }

        return finalMoves;
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
        ChessPiece currPiece = boardMain.getPiece(move.getStartPosition());

        if (currPiece == null) {
            throw new InvalidMoveException("Piece is null");
        }

        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());

        if (currPiece.getTeamColor() != turnColor) {
            throw new InvalidMoveException("Piece out of turn");
        }
        if (!validMoves.contains(move)) {
            throw new InvalidMoveException("Invalid Move. Does not pass validity test.");
        }
//                    END OF VALIDITY CHECKS AND ERRORS
//                    PROMOTION, ELSE STANDARD MOVE
        moveUpdate(move);

//                        UPDATE KING POSITION FOR CHECK TRACKING
        if (currPiece.getPieceType() == ChessPiece.PieceType.KING) {
            if (turnColor == TeamColor.BLACK) {
                blackKingPos = move.getEndPosition();
            } else {
                whiteKingPos = move.getEndPosition();
            }
        }
//                    TURN CHANGE
        if (turnColor == TeamColor.BLACK) {
            turnColor = TeamColor.WHITE;
        } else {
            turnColor = TeamColor.BLACK;
        }

    }

    /**
     * verifies the kings location
     */
    public void kingVerify(TeamColor inColor){
        ChessPosition checkPos;
        if (inColor == TeamColor.BLACK){
            checkPos = blackKingPos;
        }
        else{
            checkPos = whiteKingPos;
        }
        ChessPiece checkPiece = boardMain.getPiece(checkPos);
//        if the king for that color isn't where it should be, find and reassign
        if ((checkPiece == null) || (checkPiece.getPieceType() != ChessPiece.PieceType.KING)
                || (checkPiece.getTeamColor() != inColor)){
            for (int i = 1; i <= 8; i++){
                for (int j = 1; j <= 8; j++){
                    checkPiece = boardMain.getPiece(new ChessPosition(i, j));
                    if (checkPiece == null){continue;}
                    if ((checkPiece.getPieceType() == ChessPiece.PieceType.KING)
                            && (checkPiece.getTeamColor() == inColor)){
                        if (inColor == TeamColor.BLACK){
                            blackKingPos = new ChessPosition(i, j);
                        }
                        else{
                            whiteKingPos = new ChessPosition(i, j);
                        }
                    }
                }
            }
        }
    }

    private boolean pawnCheckHelper(int row, int col, TeamColor enemyColor){
        ChessPiece checkPiece = boardMain.getPiece(new ChessPosition(row,
                col));
        return (checkPiece != null)
                && (checkPiece.getPieceType() == ChessPiece.PieceType.PAWN)
                && (checkPiece.getTeamColor() == enemyColor);
    }

    private boolean isPawnThreat(ChessPosition currPos){

        if (currPos.equals(blackKingPos)){
            if (currPos.getRow() >= 2) {
                if (currPos.getColumn() <= 7 && pawnCheckHelper(currPos.getRow()-1,
                        currPos.getColumn()+1, TeamColor.WHITE)){
                    return true;
                }
                return currPos.getColumn() >= 2 && pawnCheckHelper(currPos.getRow() - 1,
                        currPos.getColumn() - 1, TeamColor.WHITE);
            }
        }
        else {
            if (currPos.getRow() <= 7) {
                if (currPos.getColumn() <= 7 && pawnCheckHelper(currPos.getRow()+1,
                        currPos.getColumn()+1, TeamColor.BLACK)){
                    return true;
                }
                return currPos.getColumn() >= 2 && pawnCheckHelper(currPos.getRow() + 1,
                        currPos.getColumn() - 1, TeamColor.BLACK);
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        kingVerify(TeamColor.BLACK);
        kingVerify(TeamColor.WHITE);

        ChessPosition currPos;
        ChessPiece checkPiece;
        if (teamColor == TeamColor.BLACK){
            currPos = blackKingPos;
        }
        else{
            currPos = whiteKingPos;
        }
//        ROOK QUEEN
        if (straightLineChecker(currPos, teamColor, 1, 0)){return true;}
        if (straightLineChecker(currPos, teamColor, -1, 0)){return true;}
        if (straightLineChecker(currPos, teamColor, 0, 1)){return true;}
        if (straightLineChecker(currPos, teamColor, 0, -1)){return true;}
//        BISHOP QUEEN
        if (diagonalLineChecker(currPos, teamColor, 1, 1)) {return true;}
        if (diagonalLineChecker(currPos, teamColor, 1, -1)) {return true;}
        if (diagonalLineChecker(currPos, teamColor, -1, 1)) {return true;}
        if (diagonalLineChecker(currPos, teamColor, -1, -1)) {return true;}
//        KNIGHT
        List<ChessPosition> checkingList = new ArrayList<>();

        checkingList.add(new ChessPosition(currPos.getRow()+1, currPos.getColumn()+2));
        checkingList.add(new ChessPosition(currPos.getRow()+2, currPos.getColumn()+1));

        checkingList.add(new ChessPosition(currPos.getRow()-1, currPos.getColumn()+2));
        checkingList.add(new ChessPosition(currPos.getRow()-2, currPos.getColumn()+1));

        checkingList.add(new ChessPosition(currPos.getRow()+1, currPos.getColumn()-2));
        checkingList.add(new ChessPosition(currPos.getRow()+2, currPos.getColumn()-1));

        checkingList.add(new ChessPosition(currPos.getRow()-1, currPos.getColumn()-2));
        checkingList.add(new ChessPosition(currPos.getRow()-2, currPos.getColumn()-1));

        for (ChessPosition checkPos:checkingList){
            if ((checkPos.getRow() > 8) || (checkPos.getRow() < 1) || (checkPos.getColumn() > 8)
                    || (checkPos.getColumn() < 1)){
                continue;
            }
            checkPiece = boardMain.getPiece(checkPos);
            if(checkPiece == null){
                continue;
            }
            if((checkPiece.getTeamColor() != teamColor) && (checkPiece.getPieceType() == ChessPiece.PieceType.KNIGHT)){
                return Boolean.TRUE;
            }
        }
        checkingList.clear();
//        PAWN
        if (isPawnThreat(currPos)){return true;}
//        KING
        checkingList.add(new ChessPosition(currPos.getRow()+1, currPos.getColumn()+1));
        checkingList.add(new ChessPosition(currPos.getRow()+1, currPos.getColumn()));
        checkingList.add(new ChessPosition(currPos.getRow()+1, currPos.getColumn()-1));

        checkingList.add(new ChessPosition(currPos.getRow()-1, currPos.getColumn()+1));
        checkingList.add(new ChessPosition(currPos.getRow()-1, currPos.getColumn()));
        checkingList.add(new ChessPosition(currPos.getRow()-1, currPos.getColumn()-1));

        checkingList.add(new ChessPosition(currPos.getRow(), currPos.getColumn()+1));
        checkingList.add(new ChessPosition(currPos.getRow(), currPos.getColumn()-1));
        for (ChessPosition checkPos:checkingList){
            if ((checkPos.getRow() > 8) || (checkPos.getRow() < 1)
                    || (checkPos.getColumn() > 8) || (checkPos.getColumn() < 1)){
                continue;
            }
            checkPiece = boardMain.getPiece(checkPos);
            if(checkPiece == null){
                continue;
            }

            if((checkPiece.getPieceType() == ChessPiece.PieceType.KING) && (checkPiece.getTeamColor() != teamColor)){
                return Boolean.TRUE;
            }
        }
        checkingList.clear();

        return Boolean.FALSE;
    }

    /**
     * checks all teammates moves
     * @param teamColor current team's color
     * @return true if no moves
     */
    private boolean teamHasNoMoves(TeamColor teamColor){
        kingVerify(teamColor);
        ChessPosition checkPos = (teamColor == TeamColor.BLACK) ? blackKingPos : whiteKingPos;

        Collection<ChessMove> checkMoves = validMoves(checkPos);
        if (!checkMoves.isEmpty()){
            return false;
        }
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                ChessPosition checkPosTeam = new ChessPosition(i, j);
                ChessPiece checkPieceTeam = boardMain.getPiece(checkPosTeam);
                if ((checkPieceTeam == null) || (checkPieceTeam.getTeamColor() != teamColor)
                        || (checkPieceTeam.getPieceType() == ChessPiece.PieceType.KING)){
                    continue;
                }
                checkMoves = validMoves(checkPosTeam);
                if (!checkMoves.isEmpty()){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)){
            return false;
        }
        return teamHasNoMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)){
            return false;
        }
        return teamHasNoMoves(teamColor);
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        boardMain = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return boardMain;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return turnColor == chessGame.turnColor && Objects.equals(boardMain, chessGame.boardMain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turnColor, boardMain);
    }


}
