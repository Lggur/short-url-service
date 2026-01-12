package lggur.shorturl.ui.menu.actions;

import lggur.shorturl.application.dto.EditShortLinkRequest;
import lggur.shorturl.application.usecase.EditShortLinkUseCase;
import lggur.shorturl.ui.ConsoleContext;
import lggur.shorturl.ui.input.ConsoleReader;
import lggur.shorturl.ui.output.ConsolePrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditLinkActionTest {

    @Mock
    private ConsoleContext context;
    @Mock
    private ConsoleReader reader;
    @Mock
    private ConsolePrinter printer;
    @Mock
    private EditShortLinkUseCase editUseCase;

    @InjectMocks
    private EditLinkAction action;

    @BeforeEach
    void setUp() {
        lenient().when(context.getReader()).thenReturn(reader);
        lenient().when(context.getPrinter()).thenReturn(printer);
        lenient().when(context.getEditUseCase()).thenReturn(editUseCase);
    }

    @Test
    void shouldShowErrorIfNotAuthorized() {
        when(context.hasCurrentUser()).thenReturn(false);

        action.execute(context);

        verify(printer).error("Вы должны быть авторизованы");
        verifyNoInteractions(editUseCase);
    }

    @Test
    void shouldEditLinkSuccessfully() {
        UUID userId = UUID.randomUUID();
        when(context.hasCurrentUser()).thenReturn(true);
        when(context.getCurrentUserId()).thenReturn(userId);

        when(reader.readLine(contains("код"))).thenReturn("code123");
        when(reader.readLine(contains("Новый URL"))).thenReturn("http://new-url.com");
        when(reader.readLine(contains("Новый лимит"))).thenReturn("50");

        action.execute(context);

        verify(editUseCase).execute(argThat(req -> req.getUserId().equals(userId) &&
                req.getShortCode().equals("code123") &&
                req.getNewOriginalUrl().equals("http://new-url.com") &&
                req.getNewMaxClicks().equals(50)));
        verify(printer).success("Ссылка успешно обновлена");
    }

    @Test
    void shouldSkipEmptyInputs() {
        UUID userId = UUID.randomUUID();
        when(context.hasCurrentUser()).thenReturn(true);

        when(reader.readLine(contains("код"))).thenReturn("code123");
        when(reader.readLine(contains("Новый URL"))).thenReturn("");
        when(reader.readLine(contains("Новый лимит"))).thenReturn("");

        action.execute(context);

        verify(printer).info("Изменения не внесены");
        verifyNoInteractions(editUseCase);
    }

    @Test
    void shouldHandleInvalidMaxClicksFormat() {
        UUID userId = UUID.randomUUID();
        when(context.hasCurrentUser()).thenReturn(true);
        when(context.getCurrentUserId()).thenReturn(userId);

        when(reader.readLine(contains("код"))).thenReturn("code123");
        when(reader.readLine(contains("Новый URL"))).thenReturn("");
        
        when(reader.readLine(contains("Новый лимит")))
                .thenReturn("invalid")
                .thenReturn("50");

        action.execute(context);

        verify(printer).error("Неверный формат числа");
        verify(editUseCase).execute(any());
    }
}
