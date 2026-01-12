package lggur.shorturl.ui.menu.actions;

import lggur.shorturl.core.domain.user.User;
import lggur.shorturl.core.domain.user.UserRepository;
import lggur.shorturl.ui.ConsoleContext;
import lggur.shorturl.ui.input.ConsoleReader;
import lggur.shorturl.ui.output.ConsolePrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SwitchUserActionTest {

    @Mock
    private ConsoleContext context;
    @Mock
    private ConsoleReader reader;
    @Mock
    private ConsolePrinter printer;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SwitchUserAction action;

    @BeforeEach
    void setUp() {
        lenient().when(context.getReader()).thenReturn(reader);
        lenient().when(context.getPrinter()).thenReturn(printer);
        lenient().when(context.getUserRepository()).thenReturn(userRepository);
    }

    @Test
    void shouldSwitchUserSuccessfully() {
        UUID targetUserId = UUID.randomUUID();
        User targetUser = new User(targetUserId);

        when(reader.readUUID(anyString())).thenReturn(targetUserId);
        when(userRepository.findById(targetUserId)).thenReturn(Optional.of(targetUser));
        when(context.getCurrentUser()).thenReturn(null); 

        action.execute(context);

        verify(context).setCurrentUser(targetUser);
        verify(printer).success(contains("Текущий пользователь: " + targetUserId));
    }

    @Test
    void shouldShowErrorIfUserNotFound() {
        UUID targetUserId = UUID.randomUUID();

        when(reader.readUUID(anyString())).thenReturn(targetUserId);
        when(userRepository.findById(targetUserId)).thenReturn(Optional.empty());

        action.execute(context);

        verify(printer).error(contains("не найден"));
        verify(context, never()).setCurrentUser(any());
    }

    @Test
    void shouldShowErrorIfAlreadyAuthorizedAsThatUser() {
        UUID targetUserId = UUID.randomUUID();
        User targetUser = new User(targetUserId);

        when(reader.readUUID(anyString())).thenReturn(targetUserId);
        when(userRepository.findById(targetUserId)).thenReturn(Optional.of(targetUser));
        when(context.getCurrentUser()).thenReturn(targetUser);

        action.execute(context);

        verify(printer).error("Вы уже авторизованы");
        verify(context, never()).setCurrentUser(targetUser);
    }
}
