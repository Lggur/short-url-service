package lggur.shorturl.application.usecase;

import lggur.shorturl.application.dto.EditShortLinkRequest;
import lggur.shorturl.core.domain.link.ShortLink;
import lggur.shorturl.core.domain.link.ShortLinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditShortLinkUseCaseTest {

    @Mock
    private ShortLinkRepository linkRepository;

    private EditShortLinkUseCase useCase;

    private final int minClicks = 1;
    private final int maxClicksLimit = 1000;

    @BeforeEach
    void setUp() {
        useCase = new EditShortLinkUseCase(linkRepository, minClicks, maxClicksLimit);
    }

    @Test
    @DisplayName("Должно успешно редактировать параметры собственной ссылки")
    void shouldSuccessfullyEditOwnLink() {

        UUID userId = UUID.randomUUID();
        String shortCode = "mycode";
        String oldUrl = "https://old.com";
        String newUrl = "https://new.com";
        int oldMaxClicks = 10;
        int newMaxClicks = 20;

        ShortLink link = new ShortLink(
                UUID.randomUUID(), userId, oldUrl, shortCode, oldMaxClicks,
                Instant.now(), Instant.now().plusSeconds(3600));

        when(linkRepository.findByShortCode(shortCode)).thenReturn(Optional.of(link));

        EditShortLinkRequest request = new EditShortLinkRequest(userId, shortCode, newUrl, newMaxClicks);


        useCase.execute(request);


        ArgumentCaptor<ShortLink> captor = ArgumentCaptor.forClass(ShortLink.class);
        verify(linkRepository).save(captor.capture());

        ShortLink savedLink = captor.getValue();
        assertEquals(newUrl, savedLink.getOriginalUrl());
        assertEquals(newMaxClicks, savedLink.getMaxClicks());
    }

    @Test
    @DisplayName("Должно выбрасываться SecurityException при попытке редактировать чужую ссылку")
    void shouldThrowSecurityExceptionWhenEditingOthersLink() {

        UUID ownerId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        String shortCode = "othercode";

        ShortLink link = new ShortLink(
                UUID.randomUUID(), ownerId, "https://example.com", shortCode, 10,
                Instant.now(), Instant.now().plusSeconds(3600));

        when(linkRepository.findByShortCode(shortCode)).thenReturn(Optional.of(link));

        EditShortLinkRequest request = new EditShortLinkRequest(otherUserId, shortCode,
                "https://hacker.com", 100);


        assertThrows(SecurityException.class, () -> useCase.execute(request));
        verify(linkRepository, never()).save(any());
    }

    @Test
    @DisplayName("Должно выбрасываться IllegalArgumentException, если новое максимальное количество кликов некорректно")
    void shouldThrowExceptionForInvalidMaxClicks() {

        UUID userId = UUID.randomUUID();
        String shortCode = "mycode";
        int invalidMaxClicks = maxClicksLimit + 1;

        ShortLink link = new ShortLink(
                UUID.randomUUID(), userId, "https://example.com", shortCode, 10,
                Instant.now(), Instant.now().plusSeconds(3600));

        when(linkRepository.findByShortCode(shortCode)).thenReturn(Optional.of(link));

        EditShortLinkRequest request = new EditShortLinkRequest(userId, shortCode, null, invalidMaxClicks);


        assertThrows(IllegalArgumentException.class, () -> useCase.execute(request));
        verify(linkRepository, never()).save(any());
    }
}
