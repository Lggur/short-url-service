package lggur.shorturl.application.usecase;

import lggur.shorturl.application.dto.CreateShortLinkRequest;
import lggur.shorturl.application.dto.CreateShortLinkResponse;
import lggur.shorturl.core.domain.link.ShortLink;
import lggur.shorturl.core.domain.link.ShortLinkFactory;
import lggur.shorturl.core.domain.link.ShortLinkRepository;
import lggur.shorturl.core.domain.user.User;
import lggur.shorturl.core.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateShortLinkUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ShortLinkRepository linkRepository;

    @Mock
    private ShortLinkFactory linkFactory;

    @InjectMocks
    private CreateShortLinkUseCase useCase;

    @Test
    @DisplayName("Короткий код должен быть уникальным для одного и того же URL у разных пользователей")
    void shouldCreateUniqueLinksForSameUrlByDifferentUsers() {
        String originalUrl = "https://example.com";
        UUID user1Id = UUID.randomUUID();
        UUID user2Id = UUID.randomUUID();
        User user1 = new User(user1Id);
        User user2 = new User(user2Id);

        when(userRepository.findById(user1Id)).thenReturn(Optional.of(user1));
        when(userRepository.findById(user2Id)).thenReturn(Optional.of(user2));

        when(linkRepository.findByUserIdAndOriginalUrl(user1Id, originalUrl)).thenReturn(Optional.empty());
        when(linkRepository.findByUserIdAndOriginalUrl(user2Id, originalUrl)).thenReturn(Optional.empty());

        ShortLink link1 = new ShortLink(UUID.randomUUID(), user1Id, originalUrl, "code1", 100, Instant.now(),
                Instant.now().plusSeconds(3600));
        ShortLink link2 = new ShortLink(UUID.randomUUID(), user2Id, originalUrl, "code2", 100, Instant.now(),
                Instant.now().plusSeconds(3600));

        when(linkFactory.create(user1Id, originalUrl, 10)).thenReturn(link1);
        when(linkFactory.create(user2Id, originalUrl, 10)).thenReturn(link2);

        CreateShortLinkResponse response1 = useCase.execute(new CreateShortLinkRequest(user1Id, originalUrl, 10));
        CreateShortLinkResponse response2 = useCase.execute(new CreateShortLinkRequest(user2Id, originalUrl, 10));

        assertNotNull(response1);
        assertNotNull(response2);
        assertEquals("clck.ru/code1", response1.getShortUrl());
        assertEquals("clck.ru/code2", response2.getShortUrl());

        verify(linkRepository).save(link1);
        verify(linkRepository).save(link2);
    }

    @Test
    @DisplayName("Должно выбрасываться исключение, если пользователь не найден")
    void shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        String originalUrl = "https://example.com";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        try {
            useCase.execute(new CreateShortLinkRequest(userId, originalUrl, 10));
        } catch (IllegalArgumentException e) {
            assertEquals("Пользователь не найден", e.getMessage());
        }
    }

    @Test
    @DisplayName("Должно выбрасываться исключение, если ссылка уже существует для пользователя и URL")
    void shouldThrowExceptionIfLinkAlreadyExists() {
        UUID userId = UUID.randomUUID();
        String originalUrl = "https://example.com";
        User user = new User(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ShortLink existingLink = new ShortLink(
                UUID.randomUUID(),
                userId,
                originalUrl,
                "existingCode",
                100,
                Instant.now(),
                Instant.now().plusSeconds(3600));

        when(linkRepository.findByUserIdAndOriginalUrl(userId, originalUrl))
                .thenReturn(Optional.of(existingLink));

        try {
            useCase.execute(new CreateShortLinkRequest(userId, originalUrl, 10));
        } catch (IllegalArgumentException e) {
            assertEquals(
                    "Вы уже создавали короткую ссылку для этого URL. Используйте меню редактирования, если хотите изменить параметры",
                    e.getMessage());
        }

        verify(linkFactory, never()).create(any(), any(), anyInt());
        verify(linkRepository, never()).save(any());
    }
}
