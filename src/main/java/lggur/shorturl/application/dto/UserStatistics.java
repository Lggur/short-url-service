package lggur.shorturl.application.dto;

public class UserStatistics {

    private final int totalLinks;
    private final int totalClicks;
    private final int activeLinks;

    public UserStatistics(int totalLinks, int totalClicks, int activeLinks) {
        this.totalLinks = totalLinks;
        this.totalClicks = totalClicks;
        this.activeLinks = activeLinks;
    }

    public int getTotalLinks() {
        return totalLinks;
    }

    public int getTotalClicks() {
        return totalClicks;
    }

    public int getActiveLinks() {
        return activeLinks;
    }
}
