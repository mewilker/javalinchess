package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;

import static ui.EscapeSequences.*;

public class ConsoleBoardPrinter {
    private final String letterHeader = "  a  b  c  d  e  f  g  h ";
    private final String piecesHeader = " " + A + " " + B + " " + C + " " + D + E + " " + F + " " + G + " " + H + " ";
    boolean toggleLetters = false;
    private final PrintStream out;

    public ConsoleBoardPrinter(PrintStream out){
        this.out = out;
    }
    public static void main(String[] args) {
        var game = new ChessGame();
        ConsoleBoardPrinter print = new ConsoleBoardPrinter(System.out);
        print.printGame(null, game.getBoard());
    }

    public void toggleLetters(){
        toggleLetters = !toggleLetters;
    }

    public void printGame(ChessGame.TeamColor color, ChessGame game, ChessPosition highlighted) {
        String header = toggleLetters ? letterHeader : piecesHeader;
        if (color == ChessGame.TeamColor.BLACK) {
            header = new StringBuilder(header).reverse().toString();
        }
        out.println(header);
        if (color == ChessGame.TeamColor.BLACK) {
            printBlackPerspective(game.getBoard());
        }
        else {
            printWhitePerspective(game.getBoard());
        }
        out.println(header);
    }

    private void printWhitePerspective(ChessBoard board) {
        for (int i = 8; i > 0; i--) {
            out.print(i);
            for (int j = 8; j > 0; j--) {
                printSquare(board, i, j);
            }
            out.print(SET_BG_COLOR_BLACK);
            out.println(i);
        }
    }

    private void printBlackPerspective(ChessBoard board) {
        for(int i = 1; i < 9; i++){
            out.print(i);
            for ( int j = 1; j < 9; j++){
                printSquare(board, i, j);
            }
            out.print(SET_BG_COLOR_BLACK);
            out.println(i);
        }
    }

    private void printSquare(ChessBoard board, int row, int col) {
        String squareBackground;
        if (row % 2 == 0 && col % 2 == 0 || row % 2 == 1 && col % 2 == 1) {
            squareBackground = SET_BG_COLOR_DARK_GREEN;
        }
        else{
            squareBackground = SET_BG_COLOR_LIGHT_GREY;
        }
        out.print(squareBackground);
        if (toggleLetters){
            out.print(" ");
        }
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        out.print(pieceToString(piece));
        if (toggleLetters){
            out.print(" ");
        }
    }

    private String pieceToString(ChessPiece piece){
        StringBuilder builder = new StringBuilder();
        if (piece == null){
            builder.append(toggleLetters ? " " : EMPTY );
            return builder.toString();
        }
        builder.append(piece.getTeamColor() == ChessGame.TeamColor.WHITE ? SET_TEXT_COLOR_WHITE : SET_TEXT_COLOR_BLACK);
        String pieceString =  switch (piece.getPieceType()){
            case KING -> toggleLetters ? "K" : BLACK_KING;
            case QUEEN -> toggleLetters ? "Q" : BLACK_QUEEN;
            case KNIGHT -> toggleLetters ? "N" : BLACK_KNIGHT;
            case BISHOP -> toggleLetters ? "B" : BLACK_BISHOP;
            case ROOK -> toggleLetters ? "R" : BLACK_ROOK;
            case PAWN -> toggleLetters ? "P" : BLACK_PAWN;
            case null -> toggleLetters ? " " : EMPTY;
        };

        builder.append(pieceString);
        builder.append(SET_TEXT_COLOR_BLUE);
        return builder.toString();
    }


}
