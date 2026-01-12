package lggur.shorturl.ui.menu.actions;

import lggur.shorturl.application.dto.DeleteShortLinkRequest;
import lggur.shorturl.ui.ConsoleContext;
import lggur.shorturl.ui.menu.ActionHandler;

public class DeleteLinkAction implements ActionHandler {
    @Override
    public void execute(ConsoleContext context) {
        if (!context.hasCurrentUser()) {
            context.getPrinter().error("Вы должны быть авторизованы");
            return;
        }

        try {
            String shortCode = context.getReader().readLine("Введите код короткой ссылки для удаления: ");

            boolean delete = context.getReader().readConfirmation("Вы уверены? [y/N]: ", false);
            if (!delete) {
                context.getPrinter().info("Удаление отменено");
                return;
            }

            context.getDeleteUseCase().execute(new DeleteShortLinkRequest(context.getCurrentUserId(), shortCode));

            context.getPrinter().success("Ссылка успешно удалена");

        } catch (SecurityException e) {
            context.getPrinter().error("Ошибка доступа: " + e.getMessage());
        } catch (Exception e) {
            context.getPrinter().error("Ошибка: " + e.getMessage());
        }
    }
}
