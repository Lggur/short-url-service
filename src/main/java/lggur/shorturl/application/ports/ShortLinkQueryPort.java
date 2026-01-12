package lggur.shorturl.application.ports;

import lggur.shorturl.core.domain.link.ShortLink;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ShortLinkQueryPort {
    List<ShortLink> findAll();

    List<ShortLink> findByOwnerId(UUID ownerId);

    int countByOwnerId(UUID ownerId);

    int countClicksByOwnerId(UUID ownerId);

    int countActiveByOwnerId(UUID ownerId, Instant now);
}
