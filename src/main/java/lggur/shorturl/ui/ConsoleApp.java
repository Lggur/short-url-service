package lggur.shorturl.ui;

import lggur.shorturl.ui.menu.MenuAction;

import java.util.ArrayList;
import java.util.List;

public class ConsoleApp {

    private final ConsoleContext context;

    public ConsoleApp(ConsoleContext context) {
        this.context = context;
    }

    public void run() {
        while (true) {
            List<MenuAction> actions = getAvailableActions();
            printMenu(actions);
            int choice = context.getReader().readMenuChoice("Выберите действие: ", actions.size());
            actions.get(choice - 1).execute(context);
        }
    }

    private List<MenuAction> getAvailableActions() {
        List<MenuAction> actions = new ArrayList<>();
        for (MenuAction action : MenuAction.values()) {
            if (!action.isRequiresUser() || context.hasCurrentUser()) {
                actions.add(action);
            }
        }
        return actions;
    }

    private void printMenu(List<MenuAction> actions) {
        context.getPrinter().info("\n=== Меню ===");

        if (context.hasCurrentUser()) {
            context.getPrinter().info("Текущий пользователь: " + context.getCurrentUserId());
        } else {
            context.getPrinter().info("Пользователь не выбран");
        }

        context.getPrinter().info("");
        int i = 1;
        for (MenuAction action : actions) {
            context.getPrinter().info(i++ + ". " + action.getTitle());
        }
    }

}
