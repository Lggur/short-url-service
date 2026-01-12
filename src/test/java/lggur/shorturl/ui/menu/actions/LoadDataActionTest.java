package lggur.shorturl.ui.menu.actions;

import lggur.shorturl.core.domain.link.ShortLink;
import lggur.shorturl.core.domain.user.User;
import lggur.shorturl.infra.persistence.DataPersistenceService;
import lggur.shorturl.infra.repository.InMemoryShortLinkRepository;
import lggur.shorturl.infra.repository.InMemoryUserRepository;
import lggur.shorturl.ui.ConsoleContext;
import lggur.shorturl.ui.output.ConsolePrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoadDataActionTest {

    @Mock
    private ConsoleContext context;
    @Mock
    private ConsolePrinter printer;
    @Mock
    private InMemoryUserRepository userRepository;
    @Mock
    private InMemoryShortLinkRepository linkRepository;
    @Mock
    private DataPersistenceService persistenceService;

    @InjectMocks
    private LoadDataAction action;

    @BeforeEach
    void setUp() {
        lenient().when(context.getPrinter()).thenReturn(printer);
        lenient().when(context.getInMemoryUserRepository()).thenReturn(userRepository);
        lenient().when(context.getInMemoryLinkRepository()).thenReturn(linkRepository);
        lenient().when(context.getDataPersistenceService()).thenReturn(persistenceService);
    }

    @Test
    void shouldLoadDataSuccessfully() throws Exception {
        User user = new User(UUID.randomUUID());
        ShortLink link = new ShortLink(
                UUID.randomUUID(), user.getId(), "http://example.com", "code", 100, Instant.now(), Instant.now());
        List<User> users = List.of(user);
        List<ShortLink> links = List.of(link);

        when(persistenceService.loadUsers()).thenReturn(users);
        when(persistenceService.loadLinks()).thenReturn(links);

        action.execute(context);

        verify(userRepository).clear();
        verify(linkRepository).clear();
        verify(userRepository).saveAll(users);
        verify(linkRepository).saveAll(links);
        verify(context).setCurrentUser(null);
        verify(printer).success("Данные успешно загружены из папки data/");
    }

    @Test
    void shouldHandleLoadError() throws Exception {
        when(persistenceService.loadUsers()).thenThrow(new RuntimeException("File not found"));

        action.execute(context);

        verify(printer).error("Ошибка при загрузке данных: File not found");
        verify(userRepository, never()).clear();
    }
}
