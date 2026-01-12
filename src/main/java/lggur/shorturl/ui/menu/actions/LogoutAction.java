package lggur.shorturl.ui.menu.actions;

import lggur.shorturl.ui.ConsoleContext;
import lggur.shorturl.ui.menu.ActionHandler;

public class LogoutAction implements ActionHandler {
    @Override
    public void execute(ConsoleContext context) {
        if (!context.hasCurrentUser()) {
            context.getPrinter().error("Вы не авторизованы");
            return;
        }

        context.getPrinter().success("Вы вышли из аккаунта " + context.getCurrentUserId());
        context.setCurrentUser(null);
    }
}
