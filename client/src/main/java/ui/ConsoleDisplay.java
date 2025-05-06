package ui;

import java.io.PrintStream;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ConsoleDisplay implements Display{
    private final PrintStream out;
    private final Scanner in;

    public ConsoleDisplay(PrintStream out, Scanner in){
        this.out = out;
        this.in = in;
    }

    public void title(){
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);
        out.print(SET_TEXT_BOLD);
        out.print(WHITE_QUEEN);
        out.print(" Welcome to Makenna's Chess Server!");
        out.print(WHITE_QUEEN);
        out.print("\n\n");
        out.print(RESET_TEXT_BOLD_FAINT);
        out.print(SET_TEXT_COLOR_BLUE);
    }

    public String getInput(){
        out.print(SET_TEXT_COLOR_WHITE);
        String line;
        line = in.nextLine();
        out.print(SET_TEXT_COLOR_BLUE);
        return line;

    }

    public String stringField(String prompt){
        out.print(SET_TEXT_BOLD);
        out.print(prompt + ":");
        out.print(RESET_TEXT_BOLD_FAINT);
        String result = getInput();
        out.print(SET_TEXT_COLOR_BLUE);
        return result;
    }

    //Seems a bit funny, but I want display to keep track of the print stream, not the context.
    //Context may need to keep track of specific Messages to display to the user tho
    public void printText(String toPrint){
        out.print(RESET_TEXT_BOLD_FAINT);
        out.print(RESET_TEXT_ITALIC);
        out.print(SET_TEXT_COLOR_BLUE);
        out.print(toPrint);
    }

    public int numberEntryField(String prompt){
        String numberString = stringField(prompt);
        int number = 0;
        do{
            try{
                number = Integer.parseInt(numberString);
            }
            catch (NumberFormatException e){
                printError("\"" + numberString + "\" is not a valid number. Please try again.\n");
                numberString = getInput();
            }
        } while (number <= 0);
        return number;
    }

    public void printError(String toPrint){
        out.print(SET_TEXT_COLOR_RED);
        out.print(SET_TEXT_BOLD);
        out.print(toPrint);
        out.print(SET_TEXT_COLOR_BLUE);
        out.print(RESET_TEXT_BOLD_FAINT);
    }

}
