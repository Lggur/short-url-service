package lggur.shorturl.ui.menu.actions;

import lggur.shorturl.ui.ConsoleContext;
import lggur.shorturl.ui.menu.ActionHandler;

public class OpenLinkAction implements ActionHandler {
    @Override
    public void execute(ConsoleContext context) {
        try {
            String code = context.getReader().readLine("Введите код короткой ссылки (например, AbC123): ");

            var response = context.getRedirectUseCase().execute(code);

            java.awt.Desktop.getDesktop().browse(
                    new java.net.URI(response.getOriginalUrl()));

            context.getPrinter().success("Переход выполнен");

        } catch (Exception e) {
            context.getPrinter().error(e.getMessage());
        }
    }
}
