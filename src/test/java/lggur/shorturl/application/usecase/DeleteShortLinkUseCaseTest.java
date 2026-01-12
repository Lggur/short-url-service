package lggur.shorturl.application.usecase;

import lggur.shorturl.application.dto.DeleteShortLinkRequest;
import lggur.shorturl.core.domain.link.ShortLink;
import lggur.shorturl.core.domain.link.ShortLinkRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteShortLinkUseCaseTest {

    @Mock
    private ShortLinkRepository linkRepository;

    @InjectMocks
    private DeleteShortLinkUseCase useCase;

    @Test
    @DisplayName("Должно успешно удалять собственную ссылку")
    void shouldSuccessfullyDeleteOwnLink() {

        UUID userId = UUID.randomUUID();
        String shortCode = "delete-me";

        ShortLink link = new ShortLink(
                UUID.randomUUID(), userId, "https://example.com", shortCode, 10,
                Instant.now(), Instant.now().plusSeconds(3600));

        when(linkRepository.findByShortCode(shortCode)).thenReturn(Optional.of(link));

        DeleteShortLinkRequest request = new DeleteShortLinkRequest(userId, shortCode);


        useCase.execute(request);


        verify(linkRepository).delete(link);
    }

    @Test
    @DisplayName("Должно выбрасываться SecurityException при попытке удалить чужую ссылку")
    void shouldThrowSecurityExceptionWhenDeletingOthersLink() {

        UUID ownerId = UUID.randomUUID();
        UUID hackerId = UUID.randomUUID();
        String shortCode = "cant-touch-this";

        ShortLink link = new ShortLink(
                UUID.randomUUID(), ownerId, "https://example.com", shortCode, 10,
                Instant.now(), Instant.now().plusSeconds(3600));

        when(linkRepository.findByShortCode(shortCode)).thenReturn(Optional.of(link));

        DeleteShortLinkRequest request = new DeleteShortLinkRequest(hackerId, shortCode);


        assertThrows(SecurityException.class, () -> useCase.execute(request));
        verify(linkRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Должно выбрасываться IllegalArgumentException, если ссылка не найдена")
    void shouldThrowExceptionWhenLinkNotFound() {

        UUID userId = UUID.randomUUID();
        String shortCode = "non-existent";

        when(linkRepository.findByShortCode(shortCode)).thenReturn(Optional.empty());

        DeleteShortLinkRequest request = new DeleteShortLinkRequest(userId, shortCode);


        assertThrows(IllegalArgumentException.class, () -> useCase.execute(request));
        verify(linkRepository, never()).delete(any());
    }
}
