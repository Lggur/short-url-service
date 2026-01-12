package lggur.shorturl.infra.repository;

import lggur.shorturl.core.domain.user.UserRepository;
import lggur.shorturl.core.domain.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryUserRepository implements UserRepository {

    private final Map<UUID, User> storage = new HashMap<>();

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public User save(User user) {
        storage.put(user.getId(), user);
        return user;
    }

    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    public void saveAll(List<User> users) {
        for (User user : users) {
            storage.put(user.getId(), user);
        }
    }

    public void clear() {
        storage.clear();
    }
}
