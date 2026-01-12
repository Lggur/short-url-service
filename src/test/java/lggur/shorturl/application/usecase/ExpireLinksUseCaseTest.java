package lggur.shorturl.application.usecase;

import lggur.shorturl.application.ports.Clock;
import lggur.shorturl.application.ports.NotificationPort;
import lggur.shorturl.application.ports.ShortLinkQueryPort;
import lggur.shorturl.core.domain.link.ShortLink;
import lggur.shorturl.core.domain.link.ShortLinkRepository;
import org.junit.jupiter.api.DisplayName;
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
class ExpireLinksUseCaseTest {

    @Mock
    private ShortLinkQueryPort queryPort;

    @Mock
    private ShortLinkRepository linkRepository;

    @Mock
    private NotificationPort notificationPort;

    @Mock
    private Clock clock;

    @InjectMocks
    private ExpireLinksUseCase useCase;

    @Test
    @DisplayName("Должно удалять истекшие ссылки и уведомлять их владельцев")
    void shouldDeleteExpiredLinksAndNotify() {
        
        Instant now = Instant.now();
        UUID owner1 = UUID.randomUUID();
        UUID owner2 = UUID.randomUUID();

        ShortLink expiredLink = new ShortLink(
                UUID.randomUUID(), owner1, "url1", "expired", 100,
                now.minusSeconds(7200), now.minusSeconds(3600));

        ShortLink validLink = new ShortLink(
                UUID.randomUUID(), owner2, "url2", "valid", 100,
                now.minusSeconds(100), now.plusSeconds(3600));

        when(queryPort.findAll()).thenReturn(List.of(expiredLink, validLink));
        when(clock.now()).thenReturn(now);

        
        useCase.execute();

        
        
        verify(linkRepository).delete(expiredLink);
        verify(notificationPort).notifyLinkExpired(owner1, "expired");

        
        verify(linkRepository, never()).delete(validLink);
        verify(notificationPort, never()).notifyLinkExpired(eq(owner2), any());
    }
}
