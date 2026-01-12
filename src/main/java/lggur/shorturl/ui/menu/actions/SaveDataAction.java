package lggur.shorturl.ui.menu.actions;

import lggur.shorturl.ui.ConsoleContext;
import lggur.shorturl.ui.menu.ActionHandler;

public class SaveDataAction implements ActionHandler {
    @Override
    public void execute(ConsoleContext context) {
        try {
            var users = context.getInMemoryUserRepository().findAll();
            var links = context.getInMemoryLinkRepository().findAll();

            context.getDataPersistenceService().saveData(users, links);

            context.getPrinter().success("Данные успешно сохранены в папку data/");
            context.getPrinter().info("Пользователей: " + users.size());
            context.getPrinter().info("Ссылок: " + links.size());

            long uniqueOwners = links.stream()
                    .map(lggur.shorturl.core.domain.link.ShortLink::getOwnerId)
                    .distinct()
                    .count();
            context.getPrinter().info("Создано файлов со ссылками: " + uniqueOwners);

        } catch (Exception e) {
            context.getPrinter().error("Ошибка при сохранении данных: " + e.getMessage());
        }
    }
}
