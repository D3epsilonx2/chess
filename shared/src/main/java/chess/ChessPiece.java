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
     */

    private void addPromoMoves(List<ChessMove> mainList, ChessPosition startPos, ChessPosition endPos){
        for (PieceType promoType : PieceType.values()){
            if (promoType != PieceType.KING && promoType != PieceType.PAWN){
                mainList.add(new ChessMove(startPos, endPos, promoType));
            }
        }
    }

    public List<ChessMove> getPieceMoves(ChessBoard board, ChessPosition myPosition, List<ChessMove> mainList, PieceType type){
        chess.ChessGame.TeamColor currentTeam = board.getPiece(myPosition).getTeamColor();
        if ((type == PieceType.BISHOP) || (type == PieceType.QUEEN)){
//            NorthEast
            for (int i = 1; i < 8; i++){
                if ((myPosition.getRow()+i > 8) || (myPosition.getColumn()+i > 8)){
                    break;
                }
                if (board.getPiece(new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i)) == null){
                    mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i), null));
                }
                else {
                    if (board.getPiece(new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i)).getTeamColor() != currentTeam) {
                        mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i), null));
                    }
                    break;
                }
            }
//            NorthWest
            for (int i = 1; i < 8; i++){
                if ((myPosition.getRow()+i > 8) || (myPosition.getColumn()-i < 1)){
                    break;
                }
                if (board.getPiece(new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()-i)) == null){
                    mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()-i), null));
                }
                else {
                    if (board.getPiece(new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()-i)).getTeamColor() != currentTeam) {
                        mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()-i), null));
                    }
                    break;
                }
            }
//            SouthEast
            for (int i = 1; i < 8; i++){
                if ((myPosition.getRow()-i < 1) || (myPosition.getColumn()+i > 8)){
                    break;
                }
                if (board.getPiece(new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()+i)) == null){
                    mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()+i), null));
                }
                else {
                    if (board.getPiece(new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()+i)).getTeamColor() != currentTeam) {
                        mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()+i), null));
                    }
                    break;
                }
            }
//            SouthWest
            for (int i = 1; i < 8; i++){
                if ((myPosition.getRow()-i < 1) ||(myPosition.getColumn()-i < 1)){
                    break;
                }
                if (board.getPiece(new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()-i)) == null){
                    mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()-i), null));
                }
                else {
                    if (board.getPiece(new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()-i)).getTeamColor() != currentTeam) {
                        mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()-i), null));
                    }
                    break;
                }
            }
        }
        if ((type == PieceType.ROOK) || (type == PieceType.QUEEN)){
//            North
            for (int i = 1; i < 8; i++){
                if (myPosition.getRow()+i > 8){
                    break;
                }
                if (board.getPiece(new ChessPosition(myPosition.getRow()+i, myPosition.getColumn())) == null){
                    mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()), null));
                }
                else {
                    if (board.getPiece(new ChessPosition(myPosition.getRow()+i, myPosition.getColumn())).getTeamColor() != currentTeam) {
                        mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()), null));
                    }
                    break;
                }
            }
//            South
            for (int i = 1; i < 8; i++){
                if (myPosition.getRow()-i < 1){
                    break;
                }
                if (board.getPiece(new ChessPosition(myPosition.getRow()-i, myPosition.getColumn())) == null){
                    mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()), null));
                }
                else {
                    if (board.getPiece(new ChessPosition(myPosition.getRow()-i, myPosition.getColumn())).getTeamColor() != currentTeam) {
                        mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()), null));
                    }
                    break;
                }
            }
//            East
            for (int i = 1; i < 8; i++){
                if (myPosition.getColumn()+i > 8){
                    break;
                }
                if (board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn()+i)) == null){
                    mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn()+i), null));
                }
                else {
                    if (board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn()+i)).getTeamColor() != currentTeam) {
                        mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn()+i), null));
                    }
                    break;
                }
            }
//            West
            for (int i = 1; i < 8; i++){
                if (myPosition.getColumn()-i < 1){
                    break;
                }
                if (board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn()-i)) == null){
                    mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn()-i), null));
                }
                else {
                    if (board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn()-i)).getTeamColor() != currentTeam) {
                        mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn()-i), null));
                    }
                    break;
                }
            }

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

            for (ChessPosition checkPos:knightList){
                if (!((checkPos.getRow() > 8) || (checkPos.getColumn() > 8) || (checkPos.getRow() < 1) || (checkPos.getColumn() < 1))){ //verify it's in bounds first
                    if ((board.getPiece(checkPos) == null) || (board.getPiece(checkPos).getTeamColor() != currentTeam)){
                        mainList.add(new ChessMove(myPosition, checkPos, null));
                    }
                }
            }

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

            for (ChessPosition checkPos:kingList){
                if (!((checkPos.getRow() > 8) || (checkPos.getColumn() > 8) || (checkPos.getRow() < 1) || (checkPos.getColumn() < 1))){ //verify it's in bounds first
                    if ((board.getPiece(checkPos) == null) || (board.getPiece(checkPos).getTeamColor() != currentTeam)){
                        mainList.add(new ChessMove(myPosition, checkPos, null));
                    }
                }
            }

        }
        if (type == PieceType.PAWN){
            Boolean promoBool = Boolean.FALSE;
//            BLACK
            if (currentTeam == ChessGame.TeamColor.BLACK){
                if (myPosition.getRow() == 2){
                    promoBool = Boolean.TRUE;
                }
                if (board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn())) == null){
                    if (promoBool){
                        addPromoMoves(mainList, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()));
                    }
                    else {
                        mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), null));
                    }
                    if ((myPosition.getRow() == 7) && (board.getPiece(new ChessPosition(myPosition.getRow()-2, myPosition.getColumn())) == null)){
                        mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn()), null));
                    }
                }
                if ((myPosition.getColumn() != 1) && (board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1)) != null)){
                    if (board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1)).getTeamColor() != currentTeam){
                        if (promoBool){
                            addPromoMoves(mainList, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1));
                        }
                        else {
                            mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), null));
                        }
                    }

                }
                if ((myPosition.getColumn() != 8) && (board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1)) != null)){
                    if (board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1)).getTeamColor() != currentTeam){
                        if (promoBool) {
                            addPromoMoves(mainList, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1));
                        } else {
                            mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), null));
                        }
                    }
                }

            }
//            WHITE
            else{
                if (myPosition.getRow() == 7){
                    promoBool = Boolean.TRUE;
                }
                if (board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn())) == null){
                    if (promoBool){
                        addPromoMoves(mainList, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()));
                    }
                    else {
                        mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), null));
                    }
                    if ((myPosition.getRow() == 2) && (board.getPiece(new ChessPosition(myPosition.getRow()+2, myPosition.getColumn())) == null)){
                        mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn()), null));
                    }
                }
                if ((myPosition.getColumn() != 1) && (board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1)) != null)){
                    if (board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1)).getTeamColor() != currentTeam) {
                        if (promoBool) {
                            for (PieceType promoType : PieceType.values()) {
                                if ((promoType != PieceType.KING) && (promoType != PieceType.PAWN)) {
                                    mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), promoType));
                                }
                            }
                        } else {
                            mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), null));
                        }
                    }
                }
                if ((myPosition.getColumn() != 8) && (board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1)) != null)) {
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1)).getTeamColor() != currentTeam) {
                        if (promoBool) {
                            addPromoMoves(mainList, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1));
                        } else {
                            mainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), null));
                        }
                    }
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
