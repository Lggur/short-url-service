package lggur.shorturl.core.domain.link;

import java.time.Instant;
import java.util.UUID;

public class ShortLinkFactory {

    private final ShortCodeGenerator codeGenerator;
    private final LinkLifetimePolicy lifetimePolicy;
    private final int minClicks;
    private final int maxClicksLimit;

    public ShortLinkFactory(
            ShortCodeGenerator codeGenerator,
            LinkLifetimePolicy lifetimePolicy,
            int minClicks,
            int maxClicksLimit) {
        this.codeGenerator = codeGenerator;
        this.lifetimePolicy = lifetimePolicy;
        this.minClicks = minClicks;
        this.maxClicksLimit = maxClicksLimit;
    }

    public ShortLink create(
            UUID userId,
            String originalUrl,
            int maxClicks) {
        validateMaxClicks(maxClicks);

        Instant now = Instant.now();
        Instant expiresAt = lifetimePolicy.calculateExpiration(now);

        return new ShortLink(
                UUID.randomUUID(),
                userId,
                originalUrl,
                codeGenerator.generate(originalUrl, userId.toString()),
                maxClicks,
                now,
                expiresAt);
    }

    private void validateMaxClicks(int maxClicks) {
        if (maxClicks < minClicks || maxClicks > maxClicksLimit) {
            throw new IllegalArgumentException(
                    String.format("Лимит переходов должен быть в диапазоне от %d до %d", minClicks, maxClicksLimit));
        }
    }
}
