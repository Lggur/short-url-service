package lggur.shorturl.ui.menu.actions;

import lggur.shorturl.ui.ConsoleContext;
import lggur.shorturl.ui.menu.ActionHandler;

public class LoadDataAction implements ActionHandler {
    @Override
    public void execute(ConsoleContext context) {
        try {
            var users = context.getDataPersistenceService().loadUsers();
            var links = context.getDataPersistenceService().loadLinks();

            context.getInMemoryUserRepository().clear();
            context.getInMemoryLinkRepository().clear();

            context.getInMemoryUserRepository().saveAll(users);
            context.getInMemoryLinkRepository().saveAll(links);

            context.setCurrentUser(null);

            context.getPrinter().success("Данные успешно загружены из папки data/");
            context.getPrinter().info("Загружено пользователей: " + users.size());
            context.getPrinter().info("Загружено ссылок: " + links.size());

        } catch (Exception e) {
            context.getPrinter().error("Ошибка при загрузке данных: " + e.getMessage());
        }
    }
}
