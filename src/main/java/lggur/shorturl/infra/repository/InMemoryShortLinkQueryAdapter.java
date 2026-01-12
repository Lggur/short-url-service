package lggur.shorturl.infra.repository;

import lggur.shorturl.application.ports.ShortLinkQueryPort;
import lggur.shorturl.core.domain.link.ShortLink;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class InMemoryShortLinkQueryAdapter implements ShortLinkQueryPort {

    private final InMemoryShortLinkRepository repository;

    public InMemoryShortLinkQueryAdapter(InMemoryShortLinkRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ShortLink> findAll() {
        return repository.findAll();
    }

    @Override
    public List<ShortLink> findByOwnerId(UUID ownerId) {
        return repository.findAll().stream()
                .filter(link -> link.getOwnerId().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public int countByOwnerId(UUID ownerId) {
        return (int) repository.findAll().stream()
                .filter(link -> link.getOwnerId().equals(ownerId))
                .count();
    }

    @Override
    public int countClicksByOwnerId(UUID ownerId) {
        return repository.findAll().stream()
                .filter(link -> link.getOwnerId().equals(ownerId))
                .mapToInt(ShortLink::getCurrentClicks)
                .sum();
    }

    @Override
    public int countActiveByOwnerId(UUID ownerId, Instant now) {
        return (int) repository.findAll().stream()
                .filter(link -> link.getOwnerId().equals(ownerId))
                .filter(link -> link.isAvailable(now))
                .count();
    }
}
