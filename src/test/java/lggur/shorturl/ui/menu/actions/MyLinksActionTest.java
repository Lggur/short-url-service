package lggur.shorturl.ui.menu.actions;

import lggur.shorturl.application.ports.ShortLinkQueryPort;
import lggur.shorturl.core.domain.link.ShortLink;
import lggur.shorturl.ui.ConsoleContext;
import lggur.shorturl.ui.output.ConsolePrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyLinksActionTest {

    @Mock
    private ConsoleContext context;
    @Mock
    private ConsolePrinter printer;
    @Mock
    private ShortLinkQueryPort queryPort;

    @InjectMocks
    private MyLinksAction action;

    @BeforeEach
    void setUp() {
        lenient().when(context.getPrinter()).thenReturn(printer);
        lenient().when(context.getQueryPort()).thenReturn(queryPort);
    }

    @Test
    void shouldShowErrorIfNotAuthorized() {
        when(context.hasCurrentUser()).thenReturn(false);

        action.execute(context);

        verify(printer).error("Вы должны быть авторизованы");
        verifyNoInteractions(queryPort);
    }

    @Test
    void shouldShowMessageIfNoLinksFound() {
        UUID userId = UUID.randomUUID();
        when(context.hasCurrentUser()).thenReturn(true);
        when(context.getCurrentUserId()).thenReturn(userId);
        when(queryPort.findByOwnerId(userId)).thenReturn(Collections.emptyList());

        action.execute(context);

        verify(printer).info("У вас пока нет ссылок");
    }

    @Test
    void shouldDisplayUserLinks() {
        UUID userId = UUID.randomUUID();
        when(context.hasCurrentUser()).thenReturn(true);
        when(context.getCurrentUserId()).thenReturn(userId);

        ShortLink link = new ShortLink(
                UUID.randomUUID(), userId, "https://example.com", "code123", 100,
                Instant.now(), Instant.now().plusSeconds(3600));
        when(queryPort.findByOwnerId(userId)).thenReturn(List.of(link));

        action.execute(context);

        verify(printer).info(contains("=== Ваши ссылки ==="));
        verify(printer).info(contains("code123"));
        verify(printer).info(contains("https://example.com"));
    }
}
