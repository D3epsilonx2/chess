package chess;

import com.sun.tools.javac.Main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.lang.Math;
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
     * Recursive function for gathering a list of moves
     */
    public List<ChessMove> getPiecePositions(PieceType type,ChessPosition myPosition, List<ChessMove> MainList, ChessBoard board) {
        if (type == PieceType.KNIGHT){
            List<ChessPosition> KnightList = new ArrayList<>();

            KnightList.add((new ChessPosition((myPosition.getRow() + 2), (myPosition.getColumn() + 1))));
            KnightList.add((new ChessPosition((myPosition.getRow() + 2), (myPosition.getColumn() - 1))));
            KnightList.add((new ChessPosition((myPosition.getRow() - 2), (myPosition.getColumn() + 1))));
            KnightList.add((new ChessPosition((myPosition.getRow() - 2), (myPosition.getColumn() - 1))));
            KnightList.add((new ChessPosition((myPosition.getRow() + 1), (myPosition.getColumn() + 2))));
            KnightList.add((new ChessPosition((myPosition.getRow() + 1), (myPosition.getColumn() - 2))));
            KnightList.add((new ChessPosition((myPosition.getRow() - 1), (myPosition.getColumn() + 2))));
            KnightList.add((new ChessPosition((myPosition.getRow() - 1), (myPosition.getColumn() - 2))));

            for (ChessPosition CheckPos:KnightList) {
                if (!(CheckPos.getRow() > 8 || CheckPos.getColumn() > 8 || CheckPos.getRow() < 1 || CheckPos.getColumn() < 1)){
                    if (board.getPiece(CheckPos) == null){
                        MainList.add(new ChessMove(myPosition, CheckPos, null));
                    }
                    else{
                        if (board.getPiece(CheckPos).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                            MainList.add(new ChessMove(myPosition, CheckPos, null));
                        }
                    }
                }
            }
        }
        if (type == PieceType.BISHOP || type == PieceType.QUEEN){
//            NE
            for (int i = 1; i <= 8; i++){
                if((myPosition.getRow()+i > 8) || (myPosition.getColumn()+i > 8)){
                    break;
                }
                if (board.getPiece(new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i)) != null) {
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i)).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                        break;
                    }
                    else{
                        MainList.add(new ChessMove(myPosition, (new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i)), null));
                        break;
                    }
                }
                MainList.add(new ChessMove(myPosition, (new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i)), null));
            }
//            NW
            for (int i = 1; i <= 8; i++){
                if((myPosition.getRow()+i > 8) || (myPosition.getColumn()-i < 1)){
                    break;
                }
                if (board.getPiece(new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()-i)) != null) {
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i)).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                        break;
                    }
                    else{
                        MainList.add(new ChessMove(myPosition, (new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()-i)), null));
                        break;
                    }
                }
                MainList.add(new ChessMove(myPosition, (new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()-i)), null));
            }
//            SE
            for (int i = 1; i <= 8; i++){
                if((myPosition.getRow()-i < 1) || (myPosition.getColumn()+i > 8)){
                    break;
                }
                if (board.getPiece(new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()+i)) != null) {
                    if (board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i)).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                        break;
                    }
                    else{
                        MainList.add(new ChessMove(myPosition, (new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()+i)), null));
                        break;
                    }
                }
                MainList.add(new ChessMove(myPosition, (new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()+i)), null));
            }
//            SW
            for (int i = 1; i <= 8; i++){
                if((myPosition.getRow()-i < 1) || (myPosition.getColumn()-i < 1)){
                    break;
                }
                if (board.getPiece(new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()-i)) != null) {
                    if (board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i)).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                        break;
                    }
                    else{
                        MainList.add(new ChessMove(myPosition, (new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()-i)), null));
                        break;
                    }
                }
                MainList.add(new ChessMove(myPosition, (new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()-i)), null));
            }


        }
        if (type == PieceType.ROOK || type == PieceType.QUEEN){
//              North
            for (int i = myPosition.getRow() + 1; i <= 8; i++){
                if(board.getPiece(new ChessPosition(i, myPosition.getColumn())) != null){
                    if (board.getPiece(new ChessPosition(i, myPosition.getColumn())).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                        break;
                    }
                    else{
                        MainList.add(new ChessMove(myPosition, (new ChessPosition(i, myPosition.getColumn())), null));
                        break;
                    }
                }
                MainList.add(new ChessMove(myPosition, (new ChessPosition(i, myPosition.getColumn())), null));
            }
//            South
            for (int i = myPosition.getRow() - 1; i >= 1; i--){
                if(board.getPiece(new ChessPosition(i, myPosition.getColumn())) != null) {
                    if (board.getPiece(new ChessPosition(i, myPosition.getColumn())).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                        break;
                    }
                    else{
                        MainList.add(new ChessMove(myPosition, (new ChessPosition(i, myPosition.getColumn())), null));
                        break;
                    }
                }
                MainList.add(new ChessMove(myPosition, (new ChessPosition(i, myPosition.getColumn())), null));
            }
//            West
            for (int i = myPosition.getColumn() - 1; i >= 1; i--){
                if(board.getPiece(new ChessPosition(myPosition.getRow(), i)) != null) {
                    if (board.getPiece(new ChessPosition(myPosition.getRow(), i)).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                        break;
                    }
                    else {
                        MainList.add(new ChessMove(myPosition, (new ChessPosition(myPosition.getRow(), i)), null));
                        break;
                    }
                }
                MainList.add(new ChessMove(myPosition, (new ChessPosition(myPosition.getRow(), i)), null));
            }
//            East
            for (int i = myPosition.getColumn() + 1; i <= 8; i++){
                if(board.getPiece(new ChessPosition(myPosition.getRow(), i)) != null) {
                    if (board.getPiece(new ChessPosition(myPosition.getRow(), i)).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                        break;
                    } else {
                        MainList.add(new ChessMove(myPosition, (new ChessPosition(myPosition.getRow(), i)), null));
                        break;
                    }
                }
                MainList.add(new ChessMove(myPosition, (new ChessPosition(myPosition.getRow(), i)), null));
            }
        }
        if (type == PieceType.KING){
            List<ChessPosition> KingList = new ArrayList<>();

            KingList.add(new ChessPosition((myPosition.getRow() + 1), (myPosition.getColumn() + 1)));
            KingList.add(new ChessPosition((myPosition.getRow() + 1), (myPosition.getColumn() - 1)));
            KingList.add(new ChessPosition((myPosition.getRow() + 1), (myPosition.getColumn())));
            KingList.add(new ChessPosition((myPosition.getRow()), (myPosition.getColumn() + 1)));
            KingList.add(new ChessPosition((myPosition.getRow()), (myPosition.getColumn() - 1)));
            KingList.add(new ChessPosition((myPosition.getRow() - 1), (myPosition.getColumn() + 1)));
            KingList.add(new ChessPosition((myPosition.getRow() - 1), (myPosition.getColumn() - 1)));
            KingList.add(new ChessPosition((myPosition.getRow() - 1), (myPosition.getColumn())));

            for (ChessPosition CheckPos:KingList){
                if (!(CheckPos.getRow() > 8 || CheckPos.getColumn() > 8 || CheckPos.getRow() < 1 || CheckPos.getColumn() < 1)){
                    if (board.getPiece(CheckPos) == null){
                        MainList.add(new ChessMove(myPosition, CheckPos, null));
                    }
                    else{
                        if (board.getPiece(CheckPos).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                            MainList.add(new ChessMove(myPosition, CheckPos, null));
                        }
                    }
                }
            }
        }
        if (type == PieceType.PAWN){
            if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (myPosition.getRow() < 8) {
                    if ((myPosition.getColumn() != 8) && (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1)) != null)) {
                        if (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1)).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                            if (myPosition.getRow() != 7) {
                                MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), null));
                            }
                            else{
                                for (PieceType PromoPiece : PieceType.values()){
                                    if ((PromoPiece != PieceType.PAWN) && (PromoPiece != PieceType.KING)) {
                                        MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), PromoPiece));
                                    }
                                }
                            }

                        }
                    }
                    if ((myPosition.getColumn() != 1) && (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1))) != null) {
                        if (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1)).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                            if (myPosition.getRow() != 7) {
                                MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), null));
                            }
                            else{
                                for (PieceType PromoPiece : PieceType.values()){
                                    if ((PromoPiece != PieceType.PAWN) && (PromoPiece != PieceType.KING)) {
                                        MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), PromoPiece));
                                    }
                                }
                            }
                        }
                    }
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn())) == null) {
                        if ((myPosition.getRow() == 2) && (board.getPiece(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn())) == null)) {
                            MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn()), null));
                        }
                        if (myPosition.getRow() != 7) {
                            MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), null));
                        }
                        else{
                            for (PieceType PromoPiece : PieceType.values()){
                                if ((PromoPiece != PieceType.PAWN) && (PromoPiece != PieceType.KING)) {
                                    MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), PromoPiece));
                                }
                            }
                        }
                    }
                }
            }
//            Black
            else{
                if (myPosition.getRow() > 1) {
                    if ((myPosition.getColumn() != 8) && (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1)) != null)) {
                        if (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1)).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                            if (myPosition.getRow() != 2) {
                                MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), null));
                            }
                            else{
                                for (PieceType PromoPiece : PieceType.values()){
                                    if ((PromoPiece != PieceType.PAWN) && (PromoPiece != PieceType.KING)) {
                                        MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), PromoPiece));
                                    }
                                }
                            }
                        }
                    }
                    if ((myPosition.getColumn() != 1) && (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1)) != null)) {
                        if (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1)).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                            if (myPosition.getRow() != 2) {
                                MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), null));
                            }
                            else{
                                for (PieceType PromoPiece : PieceType.values()){
                                    if ((PromoPiece != PieceType.PAWN) && (PromoPiece != PieceType.KING)) {
                                        MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), PromoPiece));
                                    }
                                }
                            }
                        }
                    }
                    if (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn())) == null) {
                        if ((myPosition.getRow() == 7) && (board.getPiece(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn())) == null)) {
                            MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn()), null));
                        }
                        if (myPosition.getRow() != 2) {
                            MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), null));
                        }
                        else{
                            for (PieceType PromoPiece : PieceType.values()){
                                if ((PromoPiece != PieceType.PAWN) && (PromoPiece != PieceType.KING)) {
                                    MainList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), PromoPiece));
                                }
                            }
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
        ChessPiece piece = board.getPiece(myPosition);

        List<ChessMove> MainList = new ArrayList<>();
        MainList = getPiecePositions(this.type, myPosition, MainList, board);

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
