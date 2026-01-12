package lggur.shorturl.infra.repository;

import lggur.shorturl.core.domain.link.ShortLink;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryShortLinkRepositoryTest {

    private InMemoryShortLinkRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryShortLinkRepository();
    }

    @Test
    void shouldSaveAndFindLink() {
        UUID id = UUID.randomUUID();
        ShortLink link = new ShortLink(id, UUID.randomUUID(), "url", "abc", 10, Instant.now(), Instant.now());

        repository.save(link);

        Optional<ShortLink> foundByCode = repository.findByShortCode("abc");
        assertTrue(foundByCode.isPresent());
        assertEquals(id, foundByCode.get().getId());

        Optional<ShortLink> foundByOwner = repository.findByUserIdAndOriginalUrl(link.getOwnerId(), "url");
        assertTrue(foundByOwner.isPresent());
        assertEquals(id, foundByOwner.get().getId());
    }

    @Test
    void shouldDeleteLink() {
        ShortLink link = new ShortLink(UUID.randomUUID(), UUID.randomUUID(), "url", "abc", 10, Instant.now(),
                Instant.now());
        repository.save(link);

        repository.delete(link);

        assertFalse(repository.findByShortCode("abc").isPresent());
    }
}
