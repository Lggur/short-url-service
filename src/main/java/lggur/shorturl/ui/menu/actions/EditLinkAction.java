package lggur.shorturl.ui.menu.actions;

import lggur.shorturl.application.dto.EditShortLinkRequest;
import lggur.shorturl.ui.ConsoleContext;
import lggur.shorturl.ui.menu.ActionHandler;

public class EditLinkAction implements ActionHandler {
    @Override
    public void execute(ConsoleContext context) {
        if (!context.hasCurrentUser()) {
            context.getPrinter().error("Вы должны быть авторизованы");
            return;
        }

        try {
            String shortCode = context.getReader().readLine("Введите код короткой ссылки для редактирования: ");

            context.getPrinter().info("Оставьте поле пустым, если не хотите менять это значение");

            String newUrl = context.getReader().readLine("Новый URL (enter для пропуска): ");
            if (newUrl.isBlank()) {
                newUrl = null;
            }

            while (true) {
                String maxClicksInput = context.getReader().readLine("Новый лимит переходов (enter для пропуска): ");
                Integer newMaxClicks = null;
                boolean inputValid = true;

                if (!maxClicksInput.isBlank()) {
                    try {
                        newMaxClicks = Integer.parseInt(maxClicksInput);
                        if (newMaxClicks <= 0) {
                            context.getPrinter().error("Лимит переходов должен быть положительным числом");
                            inputValid = false;
                        }
                    } catch (NumberFormatException e) {
                        context.getPrinter().error("Неверный формат числа");
                        inputValid = false;
                    }
                }

                if (!inputValid) {
                    continue;
                }

                if (newUrl == null && newMaxClicks == null) {
                    context.getPrinter().info("Изменения не внесены");
                    return;
                }

                try {
                    context.getEditUseCase().execute(
                            new EditShortLinkRequest(
                                    context.getCurrentUserId(),
                                    shortCode,
                                    newUrl,
                                    newMaxClicks));

                    context.getPrinter().success("Ссылка успешно обновлена");
                    break;
                } catch (IllegalArgumentException e) {
                    context.getPrinter().error(e.getMessage());
                }
            }

        } catch (SecurityException e) {
            context.getPrinter().error("Ошибка доступа: " + e.getMessage());
        } catch (Exception e) {
            context.getPrinter().error("Ошибка: " + e.getMessage());
        }
    }
}
