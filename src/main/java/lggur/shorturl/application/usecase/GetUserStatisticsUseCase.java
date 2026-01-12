package lggur.shorturl.application.usecase;

import lggur.shorturl.application.dto.UserStatistics;
import lggur.shorturl.application.ports.Clock;
import lggur.shorturl.application.ports.ShortLinkQueryPort;

import java.util.UUID;

public class GetUserStatisticsUseCase {

    private final ShortLinkQueryPort queryPort;
    private final Clock clock;

    public GetUserStatisticsUseCase(ShortLinkQueryPort queryPort, Clock clock) {
        this.queryPort = queryPort;
        this.clock = clock;
    }

    public UserStatistics execute(UUID userId) {
        int totalLinks = queryPort.countByOwnerId(userId);
        int totalClicks = queryPort.countClicksByOwnerId(userId);
        int activeLinks = queryPort.countActiveByOwnerId(userId, clock.now());

        return new UserStatistics(totalLinks, totalClicks, activeLinks);
    }
}
