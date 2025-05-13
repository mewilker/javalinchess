package chess;

import java.util.Locale;
import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    final private int row;
    final private int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public ChessPosition(String input){
        String allChars = input.strip();
        allChars = allChars.replaceAll(" ", "");
        allChars = allChars.toLowerCase(Locale.ROOT);
        if (allChars.isEmpty() || allChars.isBlank()){
            throw new IllegalArgumentException("No position supplied");
        }
        else if (allChars.length()>2){
            throw new IllegalArgumentException("Too many values for position");
        }
        else if (allChars.length()< 2){
            throw new IllegalArgumentException("Not enough values for position");
        }
        char column = allChars.charAt(0);
        int col = column - 'a' + 1;
        int row = Integer.parseInt(allChars.substring(1,2));
        if (col < 1 || col > 8 || row < 1 || row > 8){
            throw new IllegalArgumentException("Not a valid position");
        }
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return getColumnAsLetter() + row;
    }

    public String getColumnAsLetter() {
        return switch (col) {
            case 1 -> "a";
            case 2 -> "b";
            case 3 -> "c";
            case 4 -> "d";
            case 5 -> "e";
            case 6 -> "f";
            case 7 -> "g";
            case 8 -> "h";
            default -> throw new IllegalArgumentException("Not a valid chess column");
        };
    }
}
