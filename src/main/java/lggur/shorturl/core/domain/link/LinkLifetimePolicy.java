package lggur.shorturl.core.domain.link;

import java.time.Instant;

public interface LinkLifetimePolicy {
    Instant calculateExpiration(Instant createdAt);
}
