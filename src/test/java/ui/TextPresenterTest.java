package ui;

import static java.lang.System.in;
import static org.junit.jupiter.api.Assertions.*;

import core.Coord;
import core.Grid;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.junit.jupiter.api.Test;

class TextPresenterTest {

    @Test
    void whenUserChoosesOption_thenTheCorrectFunctionIsCalled() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);
        ByteArrayInputStream in = new ByteArrayInputStream("Start\n".getBytes());

        Grid grid = new Grid(3, 3);
        var ref = new Object() {
            boolean startCalled = false;
            boolean stopCalled = false;
        };
        TextPresenter presenter = new TextPresenter(out, in);
        presenter.displayOptions("prompt", Map.of(
                "Start", () -> {
                    ref.startCalled = true;
                },
                "Stop", () -> {
                    ref.stopCalled = true;
                }));
        assertTrue(ref.startCalled);
        assertFalse(ref.stopCalled);
    }

    @Test
    void whenDisplayGridIsCalled_TheGridIsSentToTheOutputStream() {
        TestIOProvider ioProvider = TestIOProvider.withInput("");
        Grid grid = new Grid(3, 3);
        TextPresenter presenter = new TextPresenter(ioProvider);
        presenter.displayGrid(grid);
        String expected =
                "     1   2   3 \n"
                        + "\n"
                        + " A   -   -   - \n"
                        + "\n"
                        + " B   -   -   - \n"
                        + "\n"
                        + " C   -   -   - \n"
                        + "\n";
        assertEquals(expected, ioProvider.getOutput());
    }


    @Test
    void whenUserChoosesStopOption_thenStopFunctionIsCalled() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);
        ByteArrayInputStream input = new ByteArrayInputStream("Stop\n".getBytes(StandardCharsets.UTF_8));

        var ref = new Object() {
            boolean startCalled = false;
            boolean stopCalled = false;
        };

        TextPresenter presenter = new TextPresenter(out, input);
        presenter.displayOptions("prompt", Map.of(
                "Start", (Runnable) () -> {
                    ref.startCalled = true;
                },
                "Stop", (Runnable) () -> {
                    ref.stopCalled = true;
                }));

        assertFalse(ref.startCalled);
        assertTrue(ref.stopCalled);
    }


    @Test
    void whenDisplayGridIsCalled_CreatesRectangularGrid() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);

        // Now uses a dummy ByteArrayInputStream instead of null
        InputStream dummyInput = new ByteArrayInputStream(new byte[0]);

        TestIOProvider ioProvider = TestIOProvider.withInput("");
        Grid grid = new Grid(2, 3);
        TextPresenter presenter = new TextPresenter(out, dummyInput);

        TextPresenter presenter = new TextPresenter(ioProvider);
        presenter.displayGrid(grid);

        String expected =
                "     1   2   3 \n"
                        + "\n"
                        + " A   -   -   - \n"
                        + "\n"
                        + " B   -   -   - \n"
                        + "\n";
        assertEquals(expected, ioProvider.getOutput());
    }

    @Test
    void whenAskForCoordinateItReturnsIT() {
        Grid grid = new Grid(2, 3);
        TestIOProvider ioProvider = TestIOProvider.withInput("B3\n");
        TextPresenter presenter = new TextPresenter(ioProvider);
        Coord expected = new Coord(2, 3);
        Coord actual = presenter.askForCoordinate(grid);
        boolean Result = expected.isEqual(actual);
        assertEquals(true, Result);
        assertEquals(expected.row, actual.row);
        assertEquals(expected.col, actual.col);
    }

    @Test
    void whenAskForCoordinateIsFalse() {
        Grid grid = new Grid(2, 3);
        TestIOProvider ioProvider = TestIOProvider.withInput("C5\nB3\n");
        TextPresenter presenter = new TextPresenter(ioProvider);
        Coord expected = new Coord(2, 3);
        Coord actual = presenter.askForCoordinate(grid);
        boolean Result = expected.isEqual(actual);
        String expected_m = "Not within the Grid!";
        assertEquals(expected_m, ioProvider.getOutput());
        assertEquals(true, Result);
    }
}
