package lggur.shorturl.core.domain.link;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ShortLinkTest {

    @Test
    @DisplayName("Должно корректно отслеживать клики и блокировать при достижении лимита")
    void shouldTrackClicks() {
        ShortLink link = new ShortLink(
                UUID.randomUUID(), UUID.randomUUID(), "url", "code", 2,
                Instant.now(), Instant.now().plusSeconds(3600));

        assertFalse(link.isClickLimitReached());

        link.registerClick();
        assertEquals(1, link.getCurrentClicks());
        assertFalse(link.isClickLimitReached());

        link.registerClick();
        assertEquals(2, link.getCurrentClicks());
        assertTrue(link.isClickLimitReached());

        assertThrows(IllegalStateException.class, link::registerClick);
    }

    @Test
    @DisplayName("Должно корректно проверять доступность по времени и количеству кликов")
    void shouldCheckAvailability() {
        Instant now = Instant.now();
        ShortLink link = new ShortLink(
                UUID.randomUUID(), UUID.randomUUID(), "url", "code", 1,
                now, now.plusSeconds(3600));

        
        assertTrue(link.isAvailable(now.plusSeconds(1800)));

        
        assertFalse(link.isAvailable(now.plusSeconds(7200)));

        
        link.registerClick();
        assertFalse(link.isAvailable(now.plusSeconds(1800)));
    }

    @Test
    @DisplayName("Должно проверять корректность обновлений URL")
    void shouldValidateUrlUpdates() {
        ShortLink link = new ShortLink(
                UUID.randomUUID(), UUID.randomUUID(), "url", "code", 10,
                Instant.now(), Instant.now().plusSeconds(3600));

        link.updateOriginalUrl("https://new.com");
        assertEquals("https://new.com", link.getOriginalUrl());

        assertThrows(IllegalArgumentException.class, () -> link.updateOriginalUrl(""));
        assertThrows(IllegalArgumentException.class, () -> link.updateOriginalUrl(null));
    }

    @Test
    @DisplayName("Должно проверять корректность обновлений максимального количества кликов")
    void shouldValidateMaxClicksUpdates() {
        ShortLink link = new ShortLink(
                UUID.randomUUID(), UUID.randomUUID(), "url", "code", 10,
                Instant.now(), Instant.now().plusSeconds(3600));

        link.registerClick(); 

        link.updateMaxClicks(5);
        assertEquals(5, link.getMaxClicks());

        
        assertThrows(IllegalArgumentException.class, () -> link.updateMaxClicks(0));

        
        assertThrows(IllegalArgumentException.class, () -> link.updateMaxClicks(-1));
    }
}
