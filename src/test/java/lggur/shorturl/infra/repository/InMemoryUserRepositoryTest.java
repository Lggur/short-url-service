package lggur.shorturl.infra.repository;

import lggur.shorturl.core.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserRepositoryTest {

    private InMemoryUserRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
    }

    @Test
    void shouldSaveAndFindUserById() {
        UUID id = UUID.randomUUID();
        User user = new User(id);

        repository.save(user);
        Optional<User> foundUser = repository.findById(id);

        assertTrue(foundUser.isPresent());
        assertEquals(id, foundUser.get().getId());
    }

    @Test
    void shouldReturnEmptyWhenUserNotFound() {
        Optional<User> foundUser = repository.findById(UUID.randomUUID());

        assertTrue(foundUser.isEmpty());
    }

    @Test
    void shouldFindAllUsers() {
        User user1 = new User(UUID.randomUUID());
        User user2 = new User(UUID.randomUUID());

        repository.save(user1);
        repository.save(user2);

        List<User> allUsers = repository.findAll();

        assertEquals(2, allUsers.size());
        assertTrue(allUsers.stream().anyMatch(u -> u.getId().equals(user1.getId())));
        assertTrue(allUsers.stream().anyMatch(u -> u.getId().equals(user2.getId())));
    }
}
