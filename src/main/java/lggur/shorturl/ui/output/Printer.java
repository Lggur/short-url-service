package lggur.shorturl.ui.output;

public interface Printer {

    void print(String prompt);

    void info(String prompt);

    void success(String prompt);

    void error(String prompt);
}
