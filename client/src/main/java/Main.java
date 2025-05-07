import chess.*;
import context.ContextManager;
import facade.ServerFacade;
import ui.ConsoleDisplay;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        String url = "http://localhost:8080";
        ServerFacade server = new ServerFacade(url);
        File file = new File("log.txt");
        PrintStream err = new PrintStream(file);
        System.setErr(err);
        try(Scanner s = new Scanner(System.in)){
            ConsoleDisplay display = new ConsoleDisplay(System.out, s);
            display.title();
            new ContextManager(display, server).run();
        }
    }
}