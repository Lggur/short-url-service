package lggur.shorturl.ui.input;

import lggur.shorturl.ui.output.Printer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsoleReaderTest {

    @Mock
    private Printer printer;

    private ConsoleReader createReader(String input) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8);
        return new ConsoleReader(scanner, printer);
    }

    @Test
    void shouldReadLine() {
        ConsoleReader reader = createReader("Тестовый ввод\n");
        String result = reader.readLine("Текст:");

        assertEquals("Тестовый ввод", result);
        verify(printer).print("Текст:");
    }

    @Test
    void shouldReadPositiveInt() {

        ConsoleReader reader = createReader("abc\n0\n10\n");

        int result = reader.readPositiveInt("Введите число:");

        assertEquals(10, result);
        verify(printer, times(2)).error(anyString());
    }

    @Test
    void shouldReadMenuChoice() {

        ConsoleReader reader = createReader("5\n2\n");

        int result = reader.readMenuChoice("Выберите вариант:", 3);

        assertEquals(2, result);
        verify(printer).error(contains("от 1 до 3"));
    }

    @Test
    void shouldReadURL() {

        ConsoleReader reader = createReader("not-a-url\nhttps://example.com\n");

        URL result = reader.readURL("Введите URL:");

        assertEquals("https://example.com", result.toString());
        verify(printer).error(anyString());
    }

    @Test
    void shouldReadUUID() {
        UUID uuid = UUID.randomUUID();

        ConsoleReader reader = createReader("bad-uuid\n" + uuid + "\n");

        UUID result = reader.readUUID("Введите UUID:");

        assertEquals(uuid, result);
        verify(printer).error(contains("Некорректный UUID"));
    }

    @Test
    void shouldReadConfirmation() {
        ConsoleReader readerYes = createReader("y\n");
        assertTrue(readerYes.readConfirmation("Подтвердить?", false));

        ConsoleReader readerNo = createReader("no\n");
        assertFalse(readerNo.readConfirmation("Подтвердить?", true));

        ConsoleReader readerDefault = createReader("\n");
        assertTrue(readerDefault.readConfirmation("Подтвердить?", true));

        ConsoleReader readerInvalid = createReader("what?\nyes\n");
        assertTrue(readerInvalid.readConfirmation("Подтвердить?", false));
        verify(printer).error(contains("введите yes/y"));
    }
}
