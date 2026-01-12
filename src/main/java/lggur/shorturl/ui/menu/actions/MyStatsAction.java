package lggur.shorturl.ui.menu.actions;

import lggur.shorturl.application.dto.UserStatistics;
import lggur.shorturl.ui.ConsoleContext;
import lggur.shorturl.ui.menu.ActionHandler;

public class MyStatsAction implements ActionHandler {
    @Override
    public void execute(ConsoleContext context) {
        if (!context.hasCurrentUser()) {
            context.getPrinter().error("Вы должны быть авторизованы");
            return;
        }

        UserStatistics stats = context.getStatisticsUseCase().execute(context.getCurrentUserId());

        context.getPrinter().info("\n=== Статистика ===");
        context.getPrinter().info("Всего ссылок: " + stats.getTotalLinks());
        context.getPrinter().info("Активных ссылок: " + stats.getActiveLinks());
        context.getPrinter().info("Всего переходов: " + stats.getTotalClicks());
    }
}
