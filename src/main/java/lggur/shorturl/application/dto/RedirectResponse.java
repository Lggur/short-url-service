package lggur.shorturl.application.dto;

public class RedirectResponse {

    private final String originalUrl;

    public RedirectResponse(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }
}
