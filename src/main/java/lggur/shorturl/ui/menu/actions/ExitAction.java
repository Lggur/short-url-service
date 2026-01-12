package lggur.shorturl.ui.menu.actions;

import lggur.shorturl.ui.ConsoleContext;
import lggur.shorturl.ui.menu.ActionHandler;

public class ExitAction implements ActionHandler {
    @Override
    public void execute(ConsoleContext context) {
        boolean save = context.getReader().readConfirmation("Сохранить данные перед выходом? [Y/n]: ", true);
        if (save) {
            try {
                var users = context.getInMemoryUserRepository().findAll();
                var links = context.getInMemoryLinkRepository().findAll();
                context.getDataPersistenceService().saveData(users, links);
                context.getPrinter().info("Данные успешно сохранены.");
            } catch (Exception e) {
                context.getPrinter().error("Ошибка при сохранении данных: " + e.getMessage());
            }
        }

        context.getPrinter().info("До свидания!");
        System.exit(0);
    }
}
