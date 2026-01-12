package lggur.shorturl.application.dto;

import java.util.UUID;

public class CreateShortLinkRequest {

    private final UUID userId;
    private final String originalUrl;
    private final int maxClicks;

    public CreateShortLinkRequest(UUID userId, String originalUrl, int maxClicks) {
        this.userId = userId;
        this.originalUrl = originalUrl;
        this.maxClicks = maxClicks;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public int getMaxClicks() {
        return maxClicks;
    }
}
