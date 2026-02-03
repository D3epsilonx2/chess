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

    public List<ChessMove> getPieceMoves(ChessBoard board, ChessPosition myPosition, List<ChessMove> MainList, PieceType type){
        chess.ChessGame.TeamColor CurrentTeam = board.getPiece(myPosition).getTeamColor();
        if ((type == PieceType.BISHOP) || (type == PieceType.QUEEN)){
//            NorthEast
            for (int i = 1; i < 8; i++){
                if ((myPosition.getRow()+i > 8) || (myPosition.getColumn()+i > 8)){
                    break;
                }
                if (board.getPiece(new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i)) == null){
                    MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i), null));
                }
                else {
                    if (board.getPiece(new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i)).getTeamColor() != CurrentTeam) {
                        MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i), null));
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
                    MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()-i), null));
                }
                else {
                    if (board.getPiece(new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()-i)).getTeamColor() != CurrentTeam) {
                        MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()-i), null));
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
                    MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()+i), null));
                }
                else {
                    if (board.getPiece(new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()+i)).getTeamColor() != CurrentTeam) {
                        MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()+i), null));
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
                    MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()-i), null));
                }
                else {
                    if (board.getPiece(new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()-i)).getTeamColor() != CurrentTeam) {
                        MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()-i), null));
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
                    MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()), null));
                }
                else {
                    if (board.getPiece(new ChessPosition(myPosition.getRow()+i, myPosition.getColumn())).getTeamColor() != CurrentTeam) {
                        MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()), null));
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
                    MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()), null));
                }
                else {
                    if (board.getPiece(new ChessPosition(myPosition.getRow()-i, myPosition.getColumn())).getTeamColor() != CurrentTeam) {
                        MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()), null));
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
                    MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn()+i), null));
                }
                else {
                    if (board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn()+i)).getTeamColor() != CurrentTeam) {
                        MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn()+i), null));
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
                    MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn()-i), null));
                }
                else {
                    if (board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn()-i)).getTeamColor() != CurrentTeam) {
                        MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn()-i), null));
                    }
                    break;
                }
            }

        }
        if (type == PieceType.KNIGHT){
            List<ChessPosition> KnightList = new ArrayList<>();

            KnightList.add(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+2));
            KnightList.add(new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()+1));

            KnightList.add(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+2));
            KnightList.add(new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()+1));

            KnightList.add(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-2));
            KnightList.add(new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()-1));

            KnightList.add(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-2));
            KnightList.add(new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()-1));

            for (ChessPosition CheckPos:KnightList){
                if (!((CheckPos.getRow() > 8) || (CheckPos.getColumn() > 8) || (CheckPos.getRow() < 1) || (CheckPos.getColumn() < 1))){ //verify it's in bounds first
                    if ((board.getPiece(CheckPos) == null) || (board.getPiece(CheckPos).getTeamColor() != CurrentTeam)){
                        MainList.add(new ChessMove(myPosition, CheckPos, null));
                    }
                }
            }

        }
        if (type == PieceType.KING){
            List<ChessPosition> KingList = new ArrayList<>();

            KingList.add(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1));
            KingList.add(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()));
            KingList.add(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1));
            KingList.add(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1));
            KingList.add(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()));
            KingList.add(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1));
            KingList.add(new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1));
            KingList.add(new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1));

            for (ChessPosition CheckPos:KingList){
                if (!((CheckPos.getRow() > 8) || (CheckPos.getColumn() > 8) || (CheckPos.getRow() < 1) || (CheckPos.getColumn() < 1))){ //verify it's in bounds first
                    if ((board.getPiece(CheckPos) == null) || (board.getPiece(CheckPos).getTeamColor() != CurrentTeam)){
                        MainList.add(new ChessMove(myPosition, CheckPos, null));
                    }
                }
            }

        }
        if (type == PieceType.PAWN){
            Boolean PromoBool = Boolean.FALSE;
//            BLACK
            if (CurrentTeam == ChessGame.TeamColor.BLACK){
                if (myPosition.getRow() == 2){
                    PromoBool = Boolean.TRUE;
                }
                if (board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn())) == null){
                    if (PromoBool){
                        for (PieceType PromoType:PieceType.values()) {
                            if ((PromoType != PieceType.KING) && (PromoType != PieceType.PAWN)) {
                                MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), PromoType));
                            }
                        }
                    }
                    else {
                        MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), null));
                    }
                    if ((myPosition.getRow() == 7) && (board.getPiece(new ChessPosition(myPosition.getRow()-2, myPosition.getColumn())) == null)){
                        MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn()), null));
                    }
                }
                if ((myPosition.getColumn() != 1) && (board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1)) != null)){
                    if (board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1)).getTeamColor() != CurrentTeam){
                        if (PromoBool){
                            for (PieceType PromoType:PieceType.values()){
                                if ((PromoType != PieceType.KING) && (PromoType != PieceType.PAWN)) {
                                    MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), PromoType));
                                }
                            }
                        }
                        else {
                            MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), null));
                        }
                    }

                }
                if ((myPosition.getColumn() != 8) && (board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1)) != null)){
                    if (board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1)).getTeamColor() != CurrentTeam){
                        if (PromoBool) {
                            for (PieceType PromoType : PieceType.values()) {
                                if ((PromoType != PieceType.KING) && (PromoType != PieceType.PAWN)) {
                                    MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), PromoType));
                                }
                            }
                        } else {
                            MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), null));
                        }
                    }
                }

            }
//            WHITE
            else{
                if (myPosition.getRow() == 7){
                    PromoBool = Boolean.TRUE;
                }
                if (board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn())) == null){
                    if (PromoBool){
                        for (PieceType PromoType:PieceType.values()) {
                            if ((PromoType != PieceType.KING) && (PromoType != PieceType.PAWN)){
                                MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), PromoType));
                            }
                        }
                    }
                    else {
                        MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), null));
                    }
                    if ((myPosition.getRow() == 2) && (board.getPiece(new ChessPosition(myPosition.getRow()+2, myPosition.getColumn())) == null)){
                        MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn()), null));
                    }
                }
                if ((myPosition.getColumn() != 1) && (board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1)) != null)){
                    if (board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1)).getTeamColor() != CurrentTeam) {
                        if (PromoBool) {
                            for (PieceType PromoType : PieceType.values()) {
                                if ((PromoType != PieceType.KING) && (PromoType != PieceType.PAWN)) {
                                    MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), PromoType));
                                }
                            }
                        } else {
                            MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), null));
                        }
                    }
                }
                if ((myPosition.getColumn() != 8) && (board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1)) != null)) {
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1)).getTeamColor() != CurrentTeam) {
                        if (PromoBool) {
                            for (PieceType PromoType : PieceType.values()) {
                                if ((PromoType != PieceType.KING) && (PromoType != PieceType.PAWN)) {
                                    MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), PromoType));
                                }
                            }
                        } else {
                            MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), null));
                        }
                    }
                }

            }
        }

        return MainList;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> MainList = new ArrayList<>();

        MainList = getPieceMoves(board, myPosition, MainList, type);
        return MainList;
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
