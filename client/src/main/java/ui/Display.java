package ui;

import datamodels.GameData;

import java.util.HashMap;

public interface Display {
    public String stringField(String prompt);
    public void printText(String toPrint);
    public int numberEntryField(String prompt);
    public void printError(String toPrint);
    public void printGamesTable(HashMap<Integer, GameData> games);
    public void printNotification(String message);
}
