package lggur.shorturl.ui.menu.actions;

import lggur.shorturl.ui.ConsoleContext;
import lggur.shorturl.ui.output.ConsolePrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogoutActionTest {

    @Mock
    private ConsoleContext context;
    @Mock
    private ConsolePrinter printer;

    @InjectMocks
    private LogoutAction action;

    @BeforeEach
    void setUp() {
        lenient().when(context.getPrinter()).thenReturn(printer);
    }

    @Test
    void shouldShowErrorIfNotAuthorized() {
        when(context.hasCurrentUser()).thenReturn(false);

        action.execute(context);

        verify(printer).error("Вы не авторизованы");
        verify(context, never()).setCurrentUser(any());
    }

    @Test
    void shouldLogoutSuccessfully() {
        UUID userId = UUID.randomUUID();
        when(context.hasCurrentUser()).thenReturn(true);
        when(context.getCurrentUserId()).thenReturn(userId);

        action.execute(context);

        verify(printer).success(contains("Вы вышли из аккаунта"));
        verify(context).setCurrentUser(null);
    }
}
