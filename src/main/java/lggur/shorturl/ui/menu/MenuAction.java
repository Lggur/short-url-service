package lggur.shorturl.ui.menu;

import lggur.shorturl.ui.ConsoleContext;
import lggur.shorturl.ui.menu.actions.*;

public enum MenuAction {

    SWITCH_USER("Выбрать пользователя", new SwitchUserAction(), false),
    CREATE_LINK("Создать короткую ссылку", new CreateLinkAction(), false),
    OPEN_LINK("Перейти по короткой ссылке", new OpenLinkAction(), false),
    MY_LINKS("Мои ссылки", new MyLinksAction(), true),
    MY_STATS("Моя статистика", new MyStatsAction(), true),
    EDIT_LINK("Редактировать ссылку", new EditLinkAction(), true),
    DELETE_LINK("Удалить ссылку", new DeleteLinkAction(), true),
    SAVE_DATA("Сохранить данные", new SaveDataAction(), false),
    LOAD_DATA("Загрузить данные", new LoadDataAction(), false),
    LOGOUT("Выйти из аккаунта", new LogoutAction(), true),
    EXIT("Выход", new ExitAction(), false);

    private final String title;
    private final ActionHandler handler;
    private final boolean requiresUser;

    MenuAction(String title, ActionHandler handler, boolean requiresUser) {
        this.title = title;
        this.handler = handler;
        this.requiresUser = requiresUser;
    }

    public void execute(ConsoleContext context) {
        handler.execute(context);
    }

    public String getTitle() {
        return title;
    }

    public boolean isRequiresUser() {
        return requiresUser;
    }
}
