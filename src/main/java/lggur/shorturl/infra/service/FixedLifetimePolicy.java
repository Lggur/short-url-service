package lggur.shorturl.infra.service;

import lggur.shorturl.core.domain.link.LinkLifetimePolicy;

import java.time.Duration;
import java.time.Instant;

public class FixedLifetimePolicy implements LinkLifetimePolicy {

    private final Duration lifetime;

    public FixedLifetimePolicy(Duration lifetime) {
        this.lifetime = lifetime;
    }

    @Override
    public Instant calculateExpiration(Instant createdAt) {
        return createdAt.plus(lifetime);
    }
}
