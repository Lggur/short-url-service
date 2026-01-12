package lggur.shorturl.ui.output;

public class ConsolePrinter implements Printer {

    @Override
    public void print(String prompt) {
        System.out.print(prompt);
    }

    @Override
    public void info(String prompt) {
        System.out.println(prompt);
    }

    @Override
    public void success(String prompt) {
        System.out.println("✔ | " + prompt);
    }

    @Override
    public void error(String prompt) {
        System.out.println("✖ | " + prompt);
    }
}
