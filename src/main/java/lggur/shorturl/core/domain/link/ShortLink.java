package lggur.shorturl.core.domain.link;

import java.time.Instant;
import java.util.UUID;

public class ShortLink {

    private final UUID id;
    private final UUID ownerId;
    private String originalUrl;
    private final String shortCode;

    private int maxClicks;
    private int currentClicks;

    private final Instant createdAt;
    private final Instant expiresAt;

    public ShortLink(
            UUID id,
            UUID ownerId,
            String originalUrl,
            String shortCode,
            int maxClicks,
            Instant createdAt,
            Instant expiresAt) {
        this.id = id;
        this.ownerId = ownerId;
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.maxClicks = maxClicks;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.currentClicks = 0;
    }

    public boolean isExpired(Instant now) {
        return now.isAfter(expiresAt);
    }

    public boolean isClickLimitReached() {
        return currentClicks >= maxClicks;
    }

    public boolean isAvailable(Instant now) {
        return !isExpired(now) && !isClickLimitReached();
    }

    public void registerClick() {
        if (isClickLimitReached()) {
            throw new IllegalStateException("Достигнут лимит переходов");
        }
        currentClicks++;
    }

    public boolean isOwnedBy(UUID userId) {
        return this.ownerId.equals(userId);
    }

    public void updateOriginalUrl(String newUrl) {
        if (newUrl == null || newUrl.isBlank()) {
            throw new IllegalArgumentException("URL не может быть пустым");
        }
        this.originalUrl = newUrl;
    }

    public void updateMaxClicks(int newMaxClicks) {
        if (newMaxClicks <= 0) {
            throw new IllegalArgumentException("Лимит переходов должен быть положительным");
        }
        if (newMaxClicks < this.currentClicks) {
            throw new IllegalArgumentException(
                    "Новый лимит переходов не может быть меньше текущего количества переходов");
        }
        this.maxClicks = newMaxClicks;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getShortCode() {
        return shortCode;
    }

    public int getMaxClicks() {
        return maxClicks;
    }

    public int getCurrentClicks() {
        return currentClicks;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}
