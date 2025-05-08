package ui;

import chess.*;

import java.io.PrintStream;
import java.util.Collection;
import java.util.HashSet;

import static ui.EscapeSequences.*;

public class ConsoleBoardPrinter {
    private final PrintStream out;
    boolean toggleLetters = false;

    public ConsoleBoardPrinter(PrintStream out) {
        this.out = out;
    }

    public static void main(String[] args) {
        var game = new ChessGame();
        ConsoleBoardPrinter print = new ConsoleBoardPrinter(System.out);
        //currently highlights an empty position (no moves). Is that the desired impl?
        ChessPosition position = new ChessPosition(5,4);
        print.printGame(ChessGame.TeamColor.BLACK, game, position);
        print.printGame(null, game, position);
    }

    public void toggleLetters() {
        toggleLetters = !toggleLetters;
    }

    public void printGame(ChessGame.TeamColor color, ChessGame game) {
        printGame(color, game, null);
    }

    public void printGame(ChessGame.TeamColor color, ChessGame game, ChessPosition highlighted) {
        HashSet<ChessPosition> validEndPositions = null;
        String piecesHeader = " " + A + " " + B + " " + C + " " + D + E + " " + F + " " + G + " " + H + " ";
        String letterHeader = "  a  b  c  d  e  f  g  h ";
        String header = toggleLetters ? letterHeader : piecesHeader;
        if (color == ChessGame.TeamColor.BLACK) {
            header = new StringBuilder(header).reverse().toString();
        }
        out.println(header);
        if (highlighted != null) {
            Collection<ChessMove> moves = game.validMoves(highlighted);
            validEndPositions = new HashSet<>();
            for (ChessMove move : moves) {
                validEndPositions.add(move.getEndPosition());
            }
        }
        if (color == ChessGame.TeamColor.BLACK) {
            printBlackPerspective(game.getBoard(), validEndPositions, highlighted);
        } else {
            printWhitePerspective(game.getBoard(), validEndPositions, highlighted);
        }
        out.println(header);
    }

    private void printWhitePerspective(ChessBoard board, Collection<ChessPosition> highlights, ChessPosition start) {
        for (int i = 8; i > 0; i--) {
            out.print(i);
            for (int j = 1; j < 9; j++) {
                printSquare(board, i, j, highlights, start);
            }
            out.print(SET_BG_COLOR_BLACK);
            out.println(i);
        }
    }

    private void printBlackPerspective(ChessBoard board, Collection<ChessPosition> highlights, ChessPosition start) {
        for (int i = 1; i < 9; i++) {
            out.print(i);
            for (int j = 8; j > 0; j--) {
                printSquare(board, i, j, highlights, start);
            }
            out.print(SET_BG_COLOR_BLACK);
            out.println(i);
        }
    }

    private void printSquare(ChessBoard board, int row, int col, Collection<ChessPosition> moves,
                             ChessPosition start) {
        String squareBackground;
        boolean highlight = false;
        ChessPosition position = new ChessPosition(row, col);
        if (start != null && moves.contains(position)) {
            highlight = true;
        }
        if (row % 2 == 0 && col % 2 == 0 || row % 2 == 1 && col % 2 == 1) {
            squareBackground = highlight ? SET_BG_COLOR_DARK_BLUE : SET_BG_COLOR_DARK_GREEN;
        } else {
            squareBackground = highlight ? SET_BG_COLOR_CYAN : SET_BG_COLOR_LIGHT_GREY;
        }
        if (position.equals(start)) {
            squareBackground = SET_BG_COLOR_YELLOW;
        }
        out.print(squareBackground);
        if (toggleLetters) {
            out.print(" ");
        }
        ChessPiece piece = board.getPiece(position);
        out.print(pieceToString(piece));
        if (toggleLetters) {
            out.print(" ");
        }
    }

    private String pieceToString(ChessPiece piece) {
        StringBuilder builder = new StringBuilder();
        if (piece == null) {
            builder.append(toggleLetters ? " " : EMPTY);
            return builder.toString();
        }
        builder.append(piece.getTeamColor() == ChessGame.TeamColor.WHITE ? SET_TEXT_COLOR_WHITE : SET_TEXT_COLOR_BLACK);
        String pieceString = switch (piece.getPieceType()) {
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
