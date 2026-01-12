package lggur.shorturl.ui.input;

import java.net.URL;
import java.util.UUID;

public interface Reader {

    String readLine(String prompt);

    int readMenuChoice(String prompt, int max);

    int readPositiveInt(String prompt);

    URL readURL(String prompt);

    UUID readUUID(String prompt);

    boolean readConfirmation(String prompt, boolean defaultYes);
}
