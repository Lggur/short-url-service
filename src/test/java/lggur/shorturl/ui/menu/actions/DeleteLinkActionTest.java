package lggur.shorturl.ui.menu.actions;

import lggur.shorturl.application.dto.DeleteShortLinkRequest;
import lggur.shorturl.application.usecase.DeleteShortLinkUseCase;
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
class DeleteLinkActionTest {

    @Mock
    private ConsoleContext context;
    @Mock
    private ConsoleReader reader;
    @Mock
    private ConsolePrinter printer;
    @Mock
    private DeleteShortLinkUseCase deleteUseCase;

    @InjectMocks
    private DeleteLinkAction action;

    @BeforeEach
    void setUp() {
        lenient().when(context.getReader()).thenReturn(reader);
        lenient().when(context.getPrinter()).thenReturn(printer);
        lenient().when(context.getDeleteUseCase()).thenReturn(deleteUseCase);
    }

    @Test
    void shouldShowErrorIfNotAuthorized() {
        when(context.hasCurrentUser()).thenReturn(false);

        action.execute(context);

        verify(printer).error("Вы должны быть авторизованы");
        verifyNoInteractions(deleteUseCase);
    }

    @Test
    void shouldDeleteLinkWhenConfirmed() {
        UUID userId = UUID.randomUUID();
        when(context.hasCurrentUser()).thenReturn(true);
        when(context.getCurrentUserId()).thenReturn(userId);
        when(reader.readLine(anyString())).thenReturn("code123");
        when(reader.readConfirmation(anyString(), eq(false))).thenReturn(true);

        action.execute(context);

        verify(deleteUseCase)
                .execute(argThat(req -> req.getUserId().equals(userId) && req.getShortCode().equals("code123")));
        verify(printer).success("Ссылка успешно удалена");
    }

    @Test
    void shouldNotDeleteLinkWhenNotConfirmed() {
        when(context.hasCurrentUser()).thenReturn(true);
        when(reader.readLine(anyString())).thenReturn("code123");
        when(reader.readConfirmation(anyString(), eq(false))).thenReturn(false);

        action.execute(context);

        verifyNoInteractions(deleteUseCase);
        verify(printer).info("Удаление отменено");
    }

    @Test
    void shouldHandleSecurityException() {
        when(context.hasCurrentUser()).thenReturn(true);
        when(context.getCurrentUserId()).thenReturn(UUID.randomUUID());
        when(reader.readLine(anyString())).thenReturn("code123");
        when(reader.readConfirmation(anyString(), eq(false))).thenReturn(true);

        doThrow(new SecurityException("Access denied")).when(deleteUseCase).execute(any());

        action.execute(context);

        verify(printer).error("Ошибка доступа: Access denied");
    }
}
