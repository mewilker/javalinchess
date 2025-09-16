package ui;

import chess.ChessGame;
import chess.ChessPosition;
import datamodels.GameData;

import java.util.HashMap;

public interface Display {
    String stringField(String prompt);

    void printText(String toPrint);

    int numberEntryField(String prompt);

    void printError(String toPrint);

    void printGamesTable(HashMap<Integer, GameData> games);

    void printNotification(String message);

    void printGame(ChessGame.TeamColor color, ChessGame game);

    void printGame(ChessGame.TeamColor color, ChessGame game, ChessPosition highlighted);

    void changeDisplayOptions();
}
