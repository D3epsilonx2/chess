package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Gets a full list of all possible moves and promotions and returns it to the pieceMoves module
     * @param mainList list to add to
     * @param startPos starting position
     * @param endPos ending position
     */
    private void addPromoMoves(List<ChessMove> mainList, ChessPosition startPos, ChessPosition endPos){
        for (PieceType promoType : PieceType.values()){
            if (promoType != PieceType.KING && promoType != PieceType.PAWN){
                mainList.add(new ChessMove(startPos, endPos, promoType));
            }
        }
    }

    /**
     * adds sliding piece moves
     * @param board the board
     * @param myPosition my position
     * @param mainList the list to add to
     * @param currentTeam current team color
     * @param rowStep how the piece moves by row
     * @param colStep how the piece moves by column
     */
    private void addSlidingPieceMoves(ChessBoard board, ChessPosition myPosition,
                                      List<ChessMove> mainList, ChessGame.TeamColor currentTeam,
                                      int rowStep, int colStep){
        for (int i = 1; i < 8; i++) {
            int newRow = myPosition.getRow() + (i * rowStep);
            int newCol = myPosition.getColumn() + (i * colStep);
            if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {
                break;
            }
            ChessPosition endPos = new ChessPosition(newRow, newCol);
            ChessPiece checkPiece = board.getPiece(endPos);
            if (checkPiece == null) {
                mainList.add(new ChessMove(myPosition, endPos, null));
            } else {
                if (checkPiece.getTeamColor() != currentTeam) {
                    mainList.add(new ChessMove(myPosition, endPos, null));
                }
                break;
            }
        }
    }

    /**
     * Adds moves from a list to a list if they are accepted
     * @param board chess board
     * @param myPosition starting position
     * @param mainList list to add to
     * @param currentTeam current team color
     * @param checkList list of positions to check and add from
     */
    private void addListedMoves(ChessBoard board, ChessPosition myPosition,
                                List<ChessMove> mainList, ChessGame.TeamColor currentTeam,
                                List<ChessPosition> checkList){
        for (ChessPosition checkPos:checkList){
            if (!((checkPos.getRow() > 8) || (checkPos.getColumn() > 8)
                    || (checkPos.getRow() < 1) || (checkPos.getColumn() < 1))){ //verify it's in bounds first
                if ((board.getPiece(checkPos) == null) || (board.getPiece(checkPos).getTeamColor() != currentTeam)){
                    mainList.add(new ChessMove(myPosition, checkPos, null));
                }
            }
        }
    }
    
    private void pawnCaptureHelp(ChessBoard board, ChessPosition myPosition,
                                 List<ChessMove> mainList, ChessGame.TeamColor currentTeam,
                                 boolean promoBool, int endRow, int endCol){
        ChessPiece checkPiece = board.getPiece(new ChessPosition(endRow, endCol));
        if (checkPiece != null && checkPiece.getTeamColor() != currentTeam){
            ChessPosition endPos = new ChessPosition(endRow, endCol);
            if (promoBool){
                addPromoMoves(mainList, myPosition, endPos);
            } else{
                mainList.add(new ChessMove(myPosition, endPos, null));
            }
        }
    }

    public List<ChessMove> getPieceMoves(ChessBoard board, ChessPosition myPosition,
                                         List<ChessMove> mainList, PieceType type){
        chess.ChessGame.TeamColor currentTeam = board.getPiece(myPosition).getTeamColor();
        if ((type == PieceType.BISHOP) || (type == PieceType.QUEEN)){
            addSlidingPieceMoves(board, myPosition, mainList, currentTeam, 1, 1);
            addSlidingPieceMoves(board, myPosition, mainList, currentTeam, 1, -1);
            addSlidingPieceMoves(board, myPosition, mainList, currentTeam, -1, 1);
            addSlidingPieceMoves(board, myPosition, mainList, currentTeam, -1, -1);
        }
        if ((type == PieceType.ROOK) || (type == PieceType.QUEEN)){
            addSlidingPieceMoves(board, myPosition, mainList, currentTeam, 1, 0);
            addSlidingPieceMoves(board, myPosition, mainList, currentTeam, -1, 0);
            addSlidingPieceMoves(board, myPosition, mainList, currentTeam, 0, 1);
            addSlidingPieceMoves(board, myPosition, mainList, currentTeam, 0, -1);
        }

        if (type == PieceType.KNIGHT){
            List<ChessPosition> knightList = new ArrayList<>();

            knightList.add(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+2));
            knightList.add(new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()+1));

            knightList.add(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+2));
            knightList.add(new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()+1));

            knightList.add(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-2));
            knightList.add(new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()-1));

            knightList.add(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-2));
            knightList.add(new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()-1));

            addListedMoves(board, myPosition, mainList, currentTeam, knightList);
        }
        if (type == PieceType.KING){
            List<ChessPosition> kingList = new ArrayList<>();

            kingList.add(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1));
            kingList.add(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()));
            kingList.add(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1));
            kingList.add(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1));
            kingList.add(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()));
            kingList.add(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1));
            kingList.add(new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1));
            kingList.add(new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1));

            addListedMoves(board, myPosition, mainList, currentTeam, kingList);
        }
        if (type == PieceType.PAWN){
            boolean promoBool = false;
//            BLACK
            if (currentTeam == ChessGame.TeamColor.BLACK){
                if (myPosition.getRow() == 2){promoBool = true;}
                if (board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn())) == null){
                    ChessPosition endPos = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
                    if (promoBool){addPromoMoves(mainList, myPosition, endPos);}
                    else {mainList.add(new ChessMove(myPosition, endPos, null));}
                    if ((myPosition.getRow() == 7) && (board.getPiece(new ChessPosition(myPosition.getRow()-2, 
                            myPosition.getColumn())) == null)){
                        mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, 
                                myPosition.getColumn()), null));
                    }
                }
                if ((myPosition.getColumn() != 1) && (board.getPiece(
                        new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1)) != null)){
                    pawnCaptureHelp(board, myPosition, mainList, currentTeam, promoBool,
                            myPosition.getRow()-1, myPosition.getColumn()-1);
                }
                if ((myPosition.getColumn() != 8) && (board.getPiece(
                        new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1)) != null)){
                    pawnCaptureHelp(board, myPosition, mainList, currentTeam, promoBool,
                            myPosition.getRow()-1, myPosition.getColumn()+1);
                }

            }
//            WHITE
            else{
                if (myPosition.getRow() == 7){promoBool = true;}
                if (board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn())) == null){
                    ChessPosition endPos = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
                    if (promoBool){addPromoMoves(mainList, myPosition, endPos);}
                    else {
                        mainList.add(new ChessMove(myPosition, endPos, null));
                    }
                    if ((myPosition.getRow() == 2) && (board.getPiece(
                            new ChessPosition(myPosition.getRow()+2, myPosition.getColumn())) == null)){
                        mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, 
                                myPosition.getColumn()), null));
                    }
                }
                if ((myPosition.getColumn() != 1) && (board.getPiece(
                        new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1)) != null)){
                    pawnCaptureHelp(board, myPosition, mainList, currentTeam, promoBool,
                            myPosition.getRow()+1, myPosition.getColumn()-1);
                }
                if ((myPosition.getColumn() != 8) && (board.getPiece(
                        new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1)) != null)) {
                    pawnCaptureHelp(board, myPosition, mainList, currentTeam, promoBool,
                            myPosition.getRow()+1, myPosition.getColumn()+1);
                }

            }
        }

        return mainList;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> mainList = new ArrayList<>();

        mainList = getPieceMoves(board, myPosition, mainList, type);
        return mainList;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
