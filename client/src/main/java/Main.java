import chess.ChessGame;
import chess.ChessPiece;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        String url = "http://localhost:8081";
        //ServerFacade server = new ServerFacade(url);
        File file = new File("log.txt");
        PrintStream err = new PrintStream(file);
        System.setErr(err);

    }
}