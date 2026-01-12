package lggur.shorturl.ui.menu.actions;

import lggur.shorturl.application.dto.CreateShortLinkRequest;
import lggur.shorturl.application.dto.CreateShortLinkResponse;
import lggur.shorturl.application.usecase.CreateShortLinkUseCase;
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

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateLinkActionTest {

    @Mock
    private ConsoleContext context;
    @Mock
    private ConsoleReader reader;
    @Mock
    private ConsolePrinter printer;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CreateShortLinkUseCase createUseCase;

    @InjectMocks
    private CreateLinkAction action;

    @BeforeEach
    void setUp() {
        lenient().when(context.getReader()).thenReturn(reader);
        lenient().when(context.getPrinter()).thenReturn(printer);
        lenient().when(context.getUserRepository()).thenReturn(userRepository);
        lenient().when(context.getCreateUseCase()).thenReturn(createUseCase);
    }

    @Test
    void shouldCreateNewUserIfNoneExists() throws Exception {
        when(context.hasCurrentUser()).thenReturn(false);
        when(reader.readURL(anyString())).thenReturn(new java.net.URI("https://example.com").toURL());
        when(reader.readPositiveInt(anyString())).thenReturn(10);

        CreateShortLinkResponse response = new CreateShortLinkResponse(UUID.randomUUID(), "clck.ru/code");
        when(createUseCase.execute(any(CreateShortLinkRequest.class))).thenReturn(response);

        action.execute(context);

        verify(userRepository).save(any(User.class));
        verify(context).setCurrentUser(any(User.class));
        verify(printer).success(contains("Создан новый пользователь"));
    }

    @Test
    void shouldCreateLinkForExistingUser() throws Exception {
        UUID userId = UUID.randomUUID();
        when(context.hasCurrentUser()).thenReturn(true);
        when(context.getCurrentUserId()).thenReturn(userId);
        when(reader.readURL(anyString())).thenReturn(new java.net.URI("https://example.com").toURL());
        when(reader.readPositiveInt(anyString())).thenReturn(10);

        CreateShortLinkResponse response = new CreateShortLinkResponse(userId, "clck.ru/code");
        when(createUseCase.execute(any(CreateShortLinkRequest.class))).thenReturn(response);

        action.execute(context);

        verify(userRepository, never()).save(any(User.class));
        verify(printer).success(contains("Короткая ссылка: clck.ru/code"));
    }

    @Test
    void shouldHandleErrorDuringCreation() throws Exception {
        when(context.hasCurrentUser()).thenReturn(true);
        when(context.getCurrentUserId()).thenReturn(UUID.randomUUID());
        when(reader.readURL(anyString())).thenReturn(new java.net.URI("https://example.com").toURL());
        when(reader.readPositiveInt(anyString())).thenReturn(10);

        when(createUseCase.execute(any(CreateShortLinkRequest.class))).thenThrow(new RuntimeException("Oops"));

        action.execute(context);

        verify(printer).error(contains("Ошибка при создании ссылки: Oops"));
    }
}
