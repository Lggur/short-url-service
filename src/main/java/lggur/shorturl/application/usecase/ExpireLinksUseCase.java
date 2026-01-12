package lggur.shorturl.application.usecase;

import lggur.shorturl.application.ports.Clock;
import lggur.shorturl.application.ports.NotificationPort;
import lggur.shorturl.application.ports.ShortLinkQueryPort;
import lggur.shorturl.core.domain.link.ShortLink;
import lggur.shorturl.core.domain.link.ShortLinkRepository;

public class ExpireLinksUseCase {

    private final ShortLinkQueryPort queryPort;
    private final ShortLinkRepository linkRepository;
    private final NotificationPort notificationPort;
    private final Clock clock;

    public ExpireLinksUseCase(
            ShortLinkQueryPort queryPort,
            ShortLinkRepository linkRepository,
            NotificationPort notificationPort,
            Clock clock
    ) {
        this.queryPort = queryPort;
        this.linkRepository = linkRepository;
        this.notificationPort = notificationPort;
        this.clock = clock;
    }

    public void execute() {
        for (ShortLink link : queryPort.findAll()) {
            if (link.isExpired(clock.now())) {
                notificationPort.notifyLinkExpired(
                        link.getOwnerId(),
                        link.getShortCode()
                );
                linkRepository.delete(link);
            }
        }
    }
}
