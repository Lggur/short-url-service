package lggur.shorturl.ui.menu.actions;

import lggur.shorturl.core.domain.link.ShortLink;
import lggur.shorturl.core.domain.user.User;
import lggur.shorturl.core.domain.user.UserRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveDataActionTest {

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
    private SaveDataAction action;

    @BeforeEach
    void setUp() {
        lenient().when(context.getPrinter()).thenReturn(printer);
        lenient().when(context.getInMemoryUserRepository()).thenReturn(userRepository);
        lenient().when(context.getInMemoryLinkRepository()).thenReturn(linkRepository);
        lenient().when(context.getDataPersistenceService()).thenReturn(persistenceService);
    }

    @Test
    void shouldSaveDataSuccessfully() throws Exception {
        User user = new User(UUID.randomUUID());
        ShortLink link = new ShortLink(
                UUID.randomUUID(), user.getId(), "http://example.com", "code", 100, Instant.now(), Instant.now());

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(linkRepository.findAll()).thenReturn(List.of(link));

        action.execute(context);

        verify(persistenceService).saveData(anyList(), anyList());
        verify(printer).success("Данные успешно сохранены в папку data/");
        verify(printer).info(contains("Пользователей: 1"));
        verify(printer).info(contains("Ссылок: 1"));
        verify(printer).info(contains("Создано файлов со ссылками: 1"));
    }

    @Test
    void shouldHandleSaveError() throws Exception {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        when(linkRepository.findAll()).thenReturn(Collections.emptyList());
        doThrow(new RuntimeException("Disk full")).when(persistenceService).saveData(anyList(), anyList());

        action.execute(context);

        verify(printer).error("Ошибка при сохранении данных: Disk full");
    }
}
