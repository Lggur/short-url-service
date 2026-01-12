package lggur.shorturl.infra.service;

import lggur.shorturl.application.ports.NotificationPort;

import java.util.UUID;

public class ConsoleNotificationService implements NotificationPort {

    @Override
    public void notifyClickLimitReached(UUID userId, String shortCode) {
        System.out.println(
                "⚠ | [Пользователь " + userId +
                        "] Достигнут лимит переходов по ссылке: " + shortCode
        );
    }

    @Override
    public void notifyLinkExpired(UUID userId, String shortCode) {
        System.out.println(
                "⚠ | [Пользователь " + userId +
                        "] Истёк срок действия ссылки: " + shortCode
        );
    }
}
