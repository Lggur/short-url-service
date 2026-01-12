package lggur.shorturl.ui.menu.actions;

import lggur.shorturl.application.dto.CreateShortLinkRequest;
import lggur.shorturl.core.domain.user.User;
import lggur.shorturl.ui.ConsoleContext;
import lggur.shorturl.ui.menu.ActionHandler;

import java.util.UUID;

public class CreateLinkAction implements ActionHandler {
    @Override
    public void execute(ConsoleContext context) {
        if (!context.hasCurrentUser()) {
            UUID newUserId = UUID.randomUUID();
            User newUser = new User(newUserId);
            context.getUserRepository().save(newUser);
            context.setCurrentUser(newUser);
            context.getPrinter().success("Создан новый пользователь: " + newUserId);
        }


        String url = context.getReader().readURL("Введите длинный URL: ").toString();

        while (true) {
            int maxClicks = context.getReader().readPositiveInt("Введите лимит переходов: ");

            try {
                var response = context.getCreateUseCase().execute(
                        new CreateShortLinkRequest(
                                context.getCurrentUserId(),
                                url,
                                maxClicks));

                context.getPrinter().success("Короткая ссылка: " + response.getShortUrl());
                break;
            } catch (IllegalArgumentException e) {
                context.getPrinter().error(e.getMessage());
            } catch (Exception e) {
                context.getPrinter().error("Ошибка при создании ссылки: " + e.getMessage());
                break;
            }
        }
    }
}
