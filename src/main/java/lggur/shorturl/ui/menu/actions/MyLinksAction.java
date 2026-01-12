package lggur.shorturl.ui.menu.actions;

import lggur.shorturl.core.domain.link.ShortLink;
import lggur.shorturl.ui.ConsoleContext;
import lggur.shorturl.ui.menu.ActionHandler;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MyLinksAction implements ActionHandler {
    @Override
    public void execute(ConsoleContext context) {
        if (!context.hasCurrentUser()) {
            context.getPrinter().error("Вы должны быть авторизованы");
            return;
        }

        List<ShortLink> links = context.getQueryPort().findByOwnerId(context.getCurrentUserId());

        if (links.isEmpty()) {
            context.getPrinter().info("У вас пока нет ссылок");
            return;
        }

        context.getPrinter().info("\n=== Ваши ссылки ===");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
                .withZone(ZoneId.systemDefault());

        for (ShortLink link : links) {
            context.getPrinter().info(String.format(
                    "- %s -> %s (клики: %d/%d, истекает: %s)",
                    link.getShortCode(),
                    link.getOriginalUrl(),
                    link.getCurrentClicks(),
                    link.getMaxClicks(),
                    formatter.format(link.getExpiresAt())));
        }
    }
}
