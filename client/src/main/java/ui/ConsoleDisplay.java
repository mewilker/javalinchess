package ui;

import datamodels.GameData;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ConsoleDisplay implements Display {
    private final PrintStream out;
    private final Scanner in;

    public ConsoleDisplay(PrintStream out, Scanner in) {
        this.out = out;
        this.in = in;
    }

    public void title() {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);
        out.print(SET_TEXT_BOLD);
        out.print(WHITE_QUEEN);
        out.print(" Welcome to Makenna's Chess Server!");
        out.print(WHITE_QUEEN);
        out.println();
        out.print(RESET_TEXT_BOLD_FAINT);
        out.print(SET_TEXT_COLOR_BLUE);
    }

    public String getInput() {
        out.print(SET_TEXT_COLOR_WHITE);
        String line;
        line = in.nextLine();
        out.print(SET_TEXT_COLOR_BLUE);
        return line;

    }

    public String stringField(String prompt) {
        out.print(SET_TEXT_BOLD);
        out.print(prompt + ":");
        out.print(RESET_TEXT_BOLD_FAINT);
        String result = getInput();
        out.print(SET_TEXT_COLOR_BLUE);
        return result;
    }

    //Seems a bit funny, but I want display to keep track of the print stream, not the context.
    //Context may need to keep track of specific Messages to display to the user tho
    public void printText(String toPrint) {
        out.print(RESET_TEXT_BOLD_FAINT);
        out.print(RESET_TEXT_ITALIC);
        out.print(SET_TEXT_COLOR_BLUE);
        out.print(toPrint);
    }

    public int numberEntryField(String prompt) {
        String numberString = stringField(prompt);
        int number = 0;
        do {
            try {
                number = Integer.parseInt(numberString);
            } catch (NumberFormatException e) {
                printError("\"" + numberString + "\" is not a valid number. Please try again.");
                numberString = getInput();
            }
        } while (number <= 0);
        return number;
    }

    public void printError(String toPrint) {
        out.print(SET_TEXT_COLOR_RED);
        out.print(SET_TEXT_BOLD);
        out.println(toPrint);
        out.print(SET_TEXT_COLOR_BLUE);
        out.print(RESET_TEXT_BOLD_FAINT);
    }

    public void printGamesTable(HashMap<Integer, GameData> games) {
        out.println();
        out.print(SET_TEXT_BOLD);
        printGameRow("NUMBER", "GAME NAME", "WHITE TEAM", "BLACK TEAM");
        out.print(RESET_TEXT_BOLD_FAINT);
        for (int i = 1; i < games.size() + 1; i++) {
            GameData game = games.get(i);
            printGameRow(Integer.toString(i), game.gameName(),
                    game.whiteUsername() == null ? "AVAILABLE" : game.whiteUsername(),
                    game.blackUsername() == null ? "AVAILABLE" : game.blackUsername());
        }
        out.print(SET_TEXT_COLOR_BLUE);
        out.println();
    }

    private void printGameRow(String id, String gameName, String whiteUsername, String blackUsername) {
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print(id + " ");
        out.print(SET_TEXT_COLOR_YELLOW);
        out.print(gameName + " ");
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(whiteUsername + " ");
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(blackUsername);
        out.print(SET_BG_COLOR_BLACK);
        out.println();
    }

    @Override
    public void printNotification(String message) {
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.println(message);
        out.print(SET_TEXT_COLOR_BLUE);
    }
}
