package lggur.shorturl.core.domain.link;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShortLinkFactoryTest {

    @Mock
    private ShortCodeGenerator codeGenerator;

    @Mock
    private LinkLifetimePolicy lifetimePolicy;

    private ShortLinkFactory factory;
    private final int minClicks = 1;
    private final int maxClicksLimit = 1000;

    @BeforeEach
    void setUp() {
        factory = new ShortLinkFactory(codeGenerator, lifetimePolicy, minClicks, maxClicksLimit);
    }

    @Test
    @DisplayName("Должен создавать ссылку с валидными параметрами")
    void shouldCreateLinkWithValidParameters() {
        
        UUID userId = UUID.randomUUID();
        String originalUrl = "https://example.com";
        int maxClicks = 100;
        String expectedCode = "code123";
        Instant now = Instant.now();
        Instant expectedExpiresAt = now.plusSeconds(3600);

        when(codeGenerator.generate(originalUrl, userId.toString())).thenReturn(expectedCode);
        when(lifetimePolicy.calculateExpiration(any())).thenReturn(expectedExpiresAt);

        
        ShortLink link = factory.create(userId, originalUrl, maxClicks);

        
        assertNotNull(link);
        assertEquals(userId, link.getOwnerId());
        assertEquals(originalUrl, link.getOriginalUrl());
        assertEquals(expectedCode, link.getShortCode());
        assertEquals(maxClicks, link.getMaxClicks());
        assertEquals(expectedExpiresAt, link.getExpiresAt());
    }

    @Test
    @DisplayName("Должен выбрасывать IllegalArgumentException, если максимальное количество кликов меньше минимального")
    void shouldThrowExceptionWhenMaxClicksBelowMinimum() {
        
        UUID userId = UUID.randomUUID();
        int invalidMaxClicks = minClicks - 1;

        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> factory.create(userId, "https://example.com", invalidMaxClicks));

        assertTrue(exception.getMessage().contains("Лимит переходов должен быть в диапазоне"));
    }

    @Test
    @DisplayName("Должен выбрасывать IllegalArgumentException, если максимальное количество кликов больше максимального")
    void shouldThrowExceptionWhenMaxClicksAboveMaximum() {
        
        UUID userId = UUID.randomUUID();
        int invalidMaxClicks = maxClicksLimit + 1;

        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> factory.create(userId, "https://example.com", invalidMaxClicks));

        assertTrue(exception.getMessage().contains("Лимит переходов должен быть в диапазоне"));
    }
}
