package ui;

public interface Display {
    public String stringField(String prompt);
    public void printText(String toPrint);
    public int numberEntryField(String prompt);
    public void printError(String toPrint);
}
