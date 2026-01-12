package lggur.shorturl.application.usecase;

import lggur.shorturl.application.dto.RedirectResponse;
import lggur.shorturl.application.ports.Clock;
import lggur.shorturl.application.ports.NotificationPort;
import lggur.shorturl.core.domain.link.ShortLink;
import lggur.shorturl.core.domain.link.ShortLinkRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedirectUseCaseTest {

    @Mock
    private ShortLinkRepository linkRepository;

    @Mock
    private NotificationPort notificationPort;

    @Mock
    private Clock clock;

    @InjectMocks
    private RedirectUseCase useCase;

    @Test
    @DisplayName("Должно блокировать доступ при достижении лимита кликов и уведомлять владельца")
    void shouldBlockAccessWhenClickLimitReached() {
        
        String shortCode = "limit";
        UUID ownerId = UUID.randomUUID();
        String originalUrl = "https://example.com";
        int maxClicks = 1;

        ShortLink link = new ShortLink(
                UUID.randomUUID(),
                ownerId,
                originalUrl,
                shortCode,
                maxClicks,
                Instant.now(),
                Instant.now().plusSeconds(3600));

        
        link.registerClick();

        when(linkRepository.findByShortCode(shortCode)).thenReturn(Optional.of(link));
        
        when(clock.now()).thenReturn(Instant.now());

        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            useCase.execute(shortCode);
        });

        assertEquals("Достигнут лимит переходов", exception.getMessage());
        verify(notificationPort).notifyClickLimitReached(ownerId, shortCode);
        
        verify(linkRepository, never()).save(any());
    }

    @Test
    @DisplayName("Должно удалять ссылку и уведомлять при её истечении")
    void shouldDeleteLinkAndNotifyWhenExpired() {
        
        String shortCode = "expired";
        UUID ownerId = UUID.randomUUID();
        Instant expiredAt = Instant.now().minusSeconds(10); 

        ShortLink link = new ShortLink(
                UUID.randomUUID(),
                ownerId,
                "https://example.com",
                shortCode,
                100,
                expiredAt.minusSeconds(3600),
                expiredAt);

        when(linkRepository.findByShortCode(shortCode)).thenReturn(Optional.of(link));
        
        when(clock.now()).thenReturn(Instant.now());

        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            useCase.execute(shortCode);
        });

        assertEquals("Срок действия ссылки истёк", exception.getMessage());
        verify(notificationPort).notifyLinkExpired(ownerId, shortCode);
        verify(linkRepository).delete(link);
    }

    @Test
    @DisplayName("Должно проверять уведомление, когда лимит кликов почти достигнут")
    void shouldVerifyNotificationWhenClickLimitJustReached() {
        
        String shortCode = "limit-reached-now";
        UUID ownerId = UUID.randomUUID();
        int maxClicks = 1;

        ShortLink link = new ShortLink(
                UUID.randomUUID(),
                ownerId,
                "https://example.com",
                shortCode,
                maxClicks,
                Instant.now(),
                Instant.now().plusSeconds(3600));

        when(linkRepository.findByShortCode(shortCode)).thenReturn(Optional.of(link));
        when(clock.now()).thenReturn(Instant.now());

        
        RedirectResponse response = useCase.execute(shortCode);

        
        assertEquals("https://example.com", response.getOriginalUrl());
        verify(linkRepository).save(link);
        verify(notificationPort).notifyClickLimitReached(ownerId, shortCode);
    }
}
