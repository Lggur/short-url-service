package lggur.shorturl.infra.persistence;

import lggur.shorturl.core.domain.link.ShortLink;
import lggur.shorturl.core.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DataPersistenceServiceTest {

    @TempDir
    Path tempDir;

    private DataPersistenceService persistenceService;

    @BeforeEach
    void setUp() {
        persistenceService = new DataPersistenceService(tempDir.toString());
    }

    @Test
    void shouldSaveAndLoadUsers() throws IOException {
        User user1 = new User(UUID.randomUUID());
        User user2 = new User(UUID.randomUUID());
        List<User> users = List.of(user1, user2);

        persistenceService.saveData(users, Collections.emptyList());

        List<User> loadedUsers = persistenceService.loadUsers();

        assertEquals(2, loadedUsers.size());
        assertTrue(loadedUsers.stream().anyMatch(u -> u.getId().equals(user1.getId())));
        assertTrue(loadedUsers.stream().anyMatch(u -> u.getId().equals(user2.getId())));
    }

    @Test
    void shouldSaveAndLoadLinks() throws IOException {
        User user = new User(UUID.randomUUID());
        java.time.Instant now = java.time.Instant.now();
        ShortLink link1 = new ShortLink(
                UUID.randomUUID(),
                user.getId(),
                "https://example.com/1",
                "code1",
                100,
                now,
                now.plusSeconds(3600));

        ShortLink link2 = new ShortLink(
                UUID.randomUUID(),
                user.getId(),
                "https://example.com/2",
                "code2",
                100,
                now,
                now.plusSeconds(3600));

        List<ShortLink> links = List.of(link1, link2);

        persistenceService.saveData(List.of(user), links);

        List<ShortLink> loadedLinks = persistenceService.loadLinks();

        assertEquals(2, loadedLinks.size());
        assertTrue(loadedLinks.stream().anyMatch(l -> l.getOriginalUrl().equals("https://example.com/1")));
    }

    @Test
    void shouldReturnEmptyListWhenNoDataFileExists() throws IOException {
        List<User> users = persistenceService.loadUsers();
        assertTrue(users.isEmpty());

        List<ShortLink> links = persistenceService.loadLinks();
        assertTrue(links.isEmpty());
    }
}
