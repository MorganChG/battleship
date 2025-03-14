package ui;

import core.Cell;
import core.Coord;
import core.Grid;
import core.Presenter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Scanner;

public class TextPresenter implements Presenter {
    private Converter converter = new DefualtConverter();
    private PrintStream output;
    private InputStream input;
    private Scanner scanner;

    // Terminal terminal;

    /**
     * @param converter
     */
    public void setConvert(Converter converter) {
        this.converter = converter;
    }

    public TextPresenter() throws IOException {
        //        terminal = TerminalBuilder.builder().providers("ffm").build();
        this(System.out, System.in);
    }

    public TextPresenter(IOProvider provider) {
        this(provider.out(), provider.in());
    }

    public TextPresenter(PrintStream output, InputStream input) {
        this.output = output;
        this.input = input;
        this.scanner = new Scanner(input);
    }

    @Override
    public void displayMessage(String s) {
        //        terminal.writer().println(s);
        output.println(s);
    }

    @Override
    public void displayGrid(Grid g) {
        // These gets us the dimensions of the grid
        int numOfRows = g.numRows();
        int numOfCols = g.numCols();
        // create a if statement that throws a error if numRows()
        // return a number higher then 26

        String[] letter = {
            " ", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
        };
        // now we need to display the grid using its rows and colunms
        // while also displaying the numbers of the x-axis and letters on the y-axis
        for (int num = 0; num <= numOfCols; num++) {
            if (num == 0) {
                output.print("   ");
            } else {
                output.printf(" %2d ", (num));
            }
        }
        output.print("\n\n");
        for (int row = 1; row <= numOfRows; row++) {
            output.print(" " + (letter[row]) + " ");
            for (int col = 1; col <= numOfCols; col++) {
                Cell cell = g.getCell(new Coord(row, col));
                setCellAs(cell);
            }
            output.print("\n\n");
        }
    }

    private void setCellAs(Cell cell) {
        String symbol = converter.convert(cell);
        output.print("  " + symbol + " ");
    }

    @Override
    public Coord askForCoordinate(Grid g) {
        Scanner scanner = new Scanner(input);
        while (true) {
            String User_input = scanner.next();
            Coord coordinate = new Coord(User_input);
            boolean answer = g.isValid(coordinate);
            if (answer) {
                return coordinate;
            } else {
                output.print("Not within the Grid!");
            }
        }
    }

    @Override
    public void displayOptions(String prompt, Map<String, Runnable> choices) {
        output.println(prompt);
        printOptions(choices);

        while (true) {
            output.print("Enter your choice: ");
            String userInput = scanner.nextLine();
            if (choices.containsKey(userInput)) {
                choices.get(userInput).run();
                return;
            } else {
                output.println("Invalid option. Please try again.");
            }
        }
    }

    private void printOptions(Map<String, Runnable> choices) {
        choices.keySet().forEach(option -> output.println("- " + option));
    }
}
