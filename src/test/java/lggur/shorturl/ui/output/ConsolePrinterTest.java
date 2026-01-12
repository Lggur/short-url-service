package lggur.shorturl.ui.output;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsolePrinterTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private ConsolePrinter printer;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        printer = new ConsolePrinter();
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void shouldPrintWithoutNewline() {
        printer.print("Hello");
        assertEquals("Hello", outContent.toString());
    }

    @Test
    void shouldPrintInfoWithNewline() {
        printer.info("Info message");
        assertEquals("Info message" + System.lineSeparator(), outContent.toString());
    }

    @Test
    void shouldPrintSuccessWithIcon() {
        printer.success("Success message");
        String expected = "✔ | Success message" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @Test
    void shouldPrintErrorWithIcon() {
        printer.error("Error message");
        String expected = "✖ | Error message" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }
}
