import chess.*;
import ui.ConsoleDisplay;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        try(Scanner s = new Scanner(System.in)){
            ConsoleDisplay display = new ConsoleDisplay(System.out, s);
            display.title();

        }
    }
}