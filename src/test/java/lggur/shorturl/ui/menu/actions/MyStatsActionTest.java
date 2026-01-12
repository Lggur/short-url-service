package lggur.shorturl.ui.menu.actions;

import lggur.shorturl.application.dto.UserStatistics;
import lggur.shorturl.application.usecase.GetUserStatisticsUseCase;
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
class MyStatsActionTest {

    @Mock
    private ConsoleContext context;
    @Mock
    private ConsolePrinter printer;
    @Mock
    private GetUserStatisticsUseCase statsUseCase;

    @InjectMocks
    private MyStatsAction action;

    @BeforeEach
    void setUp() {
        lenient().when(context.getPrinter()).thenReturn(printer);
        lenient().when(context.getStatisticsUseCase()).thenReturn(statsUseCase);
    }

    @Test
    void shouldShowErrorIfNotAuthorized() {
        when(context.hasCurrentUser()).thenReturn(false);

        action.execute(context);

        verify(printer).error("Вы должны быть авторизованы");
        verifyNoInteractions(statsUseCase);
    }

    @Test
    void shouldDisplayStatistics() {
        UUID userId = UUID.randomUUID();
        when(context.hasCurrentUser()).thenReturn(true);
        when(context.getCurrentUserId()).thenReturn(userId);

        UserStatistics stats = new UserStatistics(10, 100, 5);
        when(statsUseCase.execute(userId)).thenReturn(stats);

        action.execute(context);

        verify(printer).info(contains("=== Статистика ==="));
        verify(printer).info("Всего ссылок: 10");
        verify(printer).info("Активных ссылок: 5");
        verify(printer).info("Всего переходов: 100");
    }
}
