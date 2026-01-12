package lggur.shorturl.ui.menu.actions;

import lggur.shorturl.core.domain.user.User;
import lggur.shorturl.ui.ConsoleContext;
import lggur.shorturl.ui.menu.ActionHandler;

import java.util.UUID;

public class SwitchUserAction implements ActionHandler {
    @Override
    public void execute(ConsoleContext context) {
        UUID userId = context.getReader().readUUID("Введите UUID пользователя: ");

        User user = context.getUserRepository().findById(userId)
                .orElse(null);

        if (user == null) {
            context.getPrinter().error("Пользователь с UUID " + userId + " не найден");
            return;
        }

        if (user == context.getCurrentUser()) {
            context.getPrinter().error("Вы уже авторизованы");
            return;
        }

        context.setCurrentUser(user);
        context.getPrinter().success("Текущий пользователь: " + user.getId());
    }
}
