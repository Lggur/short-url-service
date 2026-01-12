package lggur.shorturl.application.usecase;

import lggur.shorturl.application.dto.RedirectResponse;
import lggur.shorturl.application.ports.Clock;
import lggur.shorturl.application.ports.NotificationPort;
import lggur.shorturl.core.domain.link.ShortLink;
import lggur.shorturl.core.domain.link.ShortLinkRepository;

public class RedirectUseCase {

    private final ShortLinkRepository linkRepository;
    private final NotificationPort notificationPort;
    private final Clock clock;

    public RedirectUseCase(
            ShortLinkRepository linkRepository,
            NotificationPort notificationPort,
            Clock clock
    ) {
        this.linkRepository = linkRepository;
        this.notificationPort = notificationPort;
        this.clock = clock;
    }

    public RedirectResponse execute(String shortCode) {

        ShortLink link = linkRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new IllegalArgumentException("Ссылка не найдена"));

        if (link.isExpired(clock.now())) {
            notificationPort.notifyLinkExpired(link.getOwnerId(), shortCode);
            linkRepository.delete(link);
            throw new IllegalStateException("Срок действия ссылки истёк");
        }

        if (link.isClickLimitReached()) {
            notificationPort.notifyClickLimitReached(link.getOwnerId(), shortCode);
            throw new IllegalStateException("Достигнут лимит переходов");
        }

        link.registerClick();
        linkRepository.save(link);

        if (link.isClickLimitReached()) {
            notificationPort.notifyClickLimitReached(link.getOwnerId(), shortCode);
        }

        return new RedirectResponse(link.getOriginalUrl());
    }
}
