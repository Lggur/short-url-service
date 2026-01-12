package lggur.shorturl.application.ports;

import java.util.UUID;

public interface NotificationPort {

    void notifyClickLimitReached(UUID userId, String shortCode);

    void notifyLinkExpired(UUID userId, String shortCode);
}
