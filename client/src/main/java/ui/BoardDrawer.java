package ui;

import chess.*;

import static ui.EscapeSequences.*;

public class BoardDrawer {
    private BoardColor checkerColor = BoardColor.W;

    private static final int BOARD_SIZE_IN_SQUARES = 8;

    public void drawBoard(ChessGame.TeamColor joinColor){
        System.out.print(ERASE_SCREEN);
        ChessBoard board = new ChessBoard();
        board.resetBoard();

        printHeader(joinColor);
        System.out.print("\n");

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow){
            System.out.print(SET_BG_COLOR_MAGENTA);
            if (joinColor == ChessGame.TeamColor.WHITE){
                System.out.printf(" %d ", 8 - boardRow);
            } else {
                System.out.printf(" %d ", boardRow + 1);
            }
            if (checkerColor == BoardColor.W){
                System.out.print(SET_BG_COLOR_DARK_GREEN);
            } else {
                System.out.print(SET_BG_COLOR_BLACK);
            }
            pieces(board, boardRow, 0, joinColor);
            for (int boardCol = 1; boardCol < 8; boardCol++){
                if (checkerColor == BoardColor.W){
                    checkerColor = BoardColor.B;
                    System.out.print(SET_BG_COLOR_BLACK);
                    pieces(board, boardRow, boardCol, joinColor);
                } else{
                    checkerColor = BoardColor.W;
                    System.out.print(SET_BG_COLOR_DARK_GREEN);
                    pieces(board, boardRow, boardCol, joinColor);
                }
            }
            System.out.print(SET_BG_COLOR_MAGENTA);
            if (joinColor == ChessGame.TeamColor.WHITE){
                System.out.printf(" %d ", 8 - boardRow);
            } else {
                System.out.printf(" %d ", boardRow + 1);
            }
            System.out.print(RESET_BG_COLOR);
            System.out.print("\n");
        }
        printHeader(joinColor);
        checkerColor = BoardColor.W;
    }

    private void pieces(ChessBoard board, int row, int col, ChessGame.TeamColor joinColor){
        row = row + 1;
        col = col + 1;
        if (joinColor == ChessGame.TeamColor.BLACK){
            row = 9 - row;
            col = 9 - col;
        }
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        if (piece == null){
            System.out.print(EMPTY);
        } else if (piece.getPieceType() == ChessPiece.PieceType.PAWN){
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                System.out.print(WHITE_PAWN);
            } else {
                System.out.print(BLACK_PAWN);
            }
        } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK){
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                System.out.print(WHITE_ROOK);
            } else {
                System.out.print(BLACK_ROOK);
            }
        } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP){
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                System.out.print(WHITE_BISHOP);
            } else {
                System.out.print(BLACK_BISHOP);
            }
        } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT){
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                System.out.print(WHITE_KNIGHT);
            } else {
                System.out.print(BLACK_KNIGHT);
            }
        } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN){
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                System.out.print(WHITE_QUEEN);
            } else {
                System.out.print(BLACK_QUEEN);
            }
        } else if (piece.getPieceType() == ChessPiece.PieceType.KING){
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                System.out.print(WHITE_KING);
            } else {
                System.out.print(BLACK_KING);
            }
        } else {
            System.out.print(EMPTY);
        }
    }

    private static void printHeader(ChessGame.TeamColor joinColor){
        String[] letters = (joinColor == ChessGame.TeamColor.BLACK)
            ? new String[]{"h","g","f","e","d","c","b","a"}
            : new String[]{"a","b","c","d","e","f","g","h"};
        System.out.print(SET_BG_COLOR_MAGENTA);
        System.out.print(EMPTY);
        for (String letter : letters){
            System.out.print(" " + letter + "\u2003");
        }
        System.out.print(EMPTY);
        System.out.print(RESET_BG_COLOR);
    }
}
