package lggur.shorturl.ui.menu;

import lggur.shorturl.ui.ConsoleContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MenuActionTest {

    @Mock
    private ConsoleContext context;

    @Test
    void shouldHaveValidConfiguration() {
        for (MenuAction action : MenuAction.values()) {
            assertNotNull(action.getTitle(), "Title should not be null for " + action);
            assertFalse(action.getTitle().isEmpty(), "Title should not be empty for " + action);
        }
    }

    @Test
    void shouldVerifySpecificActionsRequirements() {
        assertFalse(MenuAction.SWITCH_USER.isRequiresUser());
        assertFalse(MenuAction.CREATE_LINK.isRequiresUser());
        assertTrue(MenuAction.MY_LINKS.isRequiresUser());
        assertTrue(MenuAction.LOGOUT.isRequiresUser());
        assertFalse(MenuAction.EXIT.isRequiresUser());
    }

    @Test
    void shouldExecuteAction() {
        assertTrue(MenuAction.values().length > 0);
    }
}
