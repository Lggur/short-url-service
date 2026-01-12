package lggur.shorturl.application.dto;

import java.util.UUID;

public class EditShortLinkRequest {

    private final UUID userId;
    private final String shortCode;
    private final String newOriginalUrl;
    private final Integer newMaxClicks;

    public EditShortLinkRequest(UUID userId, String shortCode, String newOriginalUrl, Integer newMaxClicks) {
        this.userId = userId;
        this.shortCode = shortCode;
        this.newOriginalUrl = newOriginalUrl;
        this.newMaxClicks = newMaxClicks;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getNewOriginalUrl() {
        return newOriginalUrl;
    }

    public Integer getNewMaxClicks() {
        return newMaxClicks;
    }
}
