package lggur.shorturl.ui.input;

import lggur.shorturl.ui.output.Printer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class ConsoleReader implements Reader {

    private final Scanner scanner;
    private final Printer printer;

    public ConsoleReader(Scanner scanner, Printer printer) {
        this.scanner = scanner;
        this.printer = printer;
    }

    @Override
    public String readLine(String prompt) {
        printer.print(prompt);
        return scanner.nextLine().trim();
    }

    @Override
    public int readMenuChoice(String prompt, int max) {
        while (true) {
            int choice = readPositiveInt(prompt);
            if (choice > max) {
                printer.error("Введите число от 1 до " + max);
                continue;
            }
            return choice;
        }
    }

    @Override
    public int readPositiveInt(String prompt) {
        while (true) {
            printer.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value <= 0)
                    throw new NumberFormatException();
                return value;
            } catch (NumberFormatException e) {
                printer.error("Введите число > 0");
            }
        }
    }

    @Override
    public URL readURL(String prompt) {
        while (true) {
            String candidate = readLine(prompt);
            try {
                return new java.net.URI(candidate).toURL();
            } catch (java.net.URISyntaxException | MalformedURLException e) {
                printer.error("Некорректный URL. Попробуйте снова");
            }
            catch (Exception e) {
                printer.error(e.getMessage());
            }
        }
    }

    @Override
    public java.util.UUID readUUID(String prompt) {
        while (true) {
            String candidate = readLine(prompt);
            try {
                return java.util.UUID.fromString(candidate);
            } catch (IllegalArgumentException e) {
                printer.error("Некорректный UUID. Попробуйте снова");
            }
        }
    }

    @Override
    public boolean readConfirmation(String prompt, boolean defaultYes) {
        while (true) {
            String input = readLine(prompt).trim().toLowerCase();

            if (input.isEmpty()) {
                return defaultYes;
            }

            switch (input) {
                case "yes":
                case "y":
                case "да":
                case "д":
                    return true;
                case "no":
                case "n":
                case "нет":
                case "н":
                    return false;
                default:
                    printer.error("Пожалуйста, введите yes/y/да/д или no/n/нет/н");
            }
        }
    }
}
