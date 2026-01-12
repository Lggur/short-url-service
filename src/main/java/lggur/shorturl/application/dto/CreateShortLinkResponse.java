package lggur.shorturl.application.dto;

import java.util.UUID;

public class CreateShortLinkResponse {

    private final UUID userId;
    private final String shortUrl;

    public CreateShortLinkResponse(UUID userId, String shortUrl) {
        this.userId = userId;
        this.shortUrl = shortUrl;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getShortUrl() {
        return shortUrl;
    }
}
