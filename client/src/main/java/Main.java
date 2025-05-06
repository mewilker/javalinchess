import chess.*;
import context.ContextManager;
import facade.ServerFacade;
import ui.ConsoleDisplay;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        String url = "http://localhost:8080";
        ServerFacade server = new ServerFacade(url);
        try(Scanner s = new Scanner(System.in)){
            ConsoleDisplay display = new ConsoleDisplay(System.out, s);
            display.title();
            new ContextManager(display, server).run();
        }
    }
}