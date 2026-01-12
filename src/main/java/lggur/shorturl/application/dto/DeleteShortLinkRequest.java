package lggur.shorturl.application.dto;

import java.util.UUID;

public class DeleteShortLinkRequest {

    private final UUID userId;
    private final String shortCode;

    public DeleteShortLinkRequest(UUID userId, String shortCode) {
        this.userId = userId;
        this.shortCode = shortCode;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getShortCode() {
        return shortCode;
    }
}
