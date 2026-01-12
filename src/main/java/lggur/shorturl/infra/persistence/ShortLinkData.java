package lggur.shorturl.infra.persistence;

import lggur.shorturl.core.domain.link.ShortLink;

import java.time.Instant;
import java.util.UUID;

public class ShortLinkData {

    private String id;
    private String ownerId;
    private String originalUrl;
    private String shortCode;
    private int maxClicks;
    private int currentClicks;
    private String createdAt;
    private String expiresAt;

    public ShortLinkData() {
    }

    public ShortLinkData(
            String id,
            String ownerId,
            String originalUrl,
            String shortCode,
            int maxClicks,
            int currentClicks,
            String createdAt,
            String expiresAt) {
        this.id = id;
        this.ownerId = ownerId;
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.maxClicks = maxClicks;
        this.currentClicks = currentClicks;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public static ShortLinkData fromShortLink(ShortLink link) {
        return new ShortLinkData(
                link.getId().toString(),
                link.getOwnerId().toString(),
                link.getOriginalUrl(),
                link.getShortCode(),
                link.getMaxClicks(),
                link.getCurrentClicks(),
                link.getCreatedAt().toString(),
                link.getExpiresAt().toString());
    }

    public ShortLink toShortLink() {
        ShortLink link = new ShortLink(
                UUID.fromString(id),
                UUID.fromString(ownerId),
                originalUrl,
                shortCode,
                maxClicks,
                Instant.parse(createdAt),
                Instant.parse(expiresAt)
        );
        for (int i = 0; i < currentClicks; i++) {
            link.registerClick();
        }
        return link;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public int getMaxClicks() {
        return maxClicks;
    }

    public void setMaxClicks(int maxClicks) {
        this.maxClicks = maxClicks;
    }

    public int getCurrentClicks() {
        return currentClicks;
    }

    public void setCurrentClicks(int currentClicks) {
        this.currentClicks = currentClicks;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }
}
