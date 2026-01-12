package lggur.shorturl.application.usecase;

import lggur.shorturl.application.dto.UserStatistics;
import lggur.shorturl.application.ports.Clock;
import lggur.shorturl.application.ports.ShortLinkQueryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserStatisticsUseCaseTest {

    @Mock
    private ShortLinkQueryPort queryPort;

    @Mock
    private Clock clock;

    @InjectMocks
    private GetUserStatisticsUseCase useCase;

    @Test
    void shouldAggregateStatisticsCorrectly() {
        UUID userId = UUID.randomUUID();
        Instant now = Instant.now();

        when(clock.now()).thenReturn(now);
        when(queryPort.countByOwnerId(userId)).thenReturn(10);
        when(queryPort.countClicksByOwnerId(userId)).thenReturn(150);
        when(queryPort.countActiveByOwnerId(userId, now)).thenReturn(5);

        UserStatistics stats = useCase.execute(userId);

        assertEquals(10, stats.getTotalLinks());
        assertEquals(150, stats.getTotalClicks());
        assertEquals(5, stats.getActiveLinks());
    }
}
