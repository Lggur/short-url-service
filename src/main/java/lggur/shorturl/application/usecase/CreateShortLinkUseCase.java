package lggur.shorturl.application.usecase;

import lggur.shorturl.application.dto.CreateShortLinkRequest;
import lggur.shorturl.application.dto.CreateShortLinkResponse;
import lggur.shorturl.core.domain.user.UserRepository;
import lggur.shorturl.core.domain.link.ShortLink;
import lggur.shorturl.core.domain.link.ShortLinkFactory;
import lggur.shorturl.core.domain.link.ShortLinkRepository;
import lggur.shorturl.core.domain.user.User;

import java.util.UUID;

public class CreateShortLinkUseCase {

    private final UserRepository userRepository;
    private final ShortLinkRepository linkRepository;
    private final ShortLinkFactory linkFactory;

    public CreateShortLinkUseCase(
            UserRepository userRepository,
            ShortLinkRepository linkRepository,
            ShortLinkFactory linkFactory) {
        this.userRepository = userRepository;
        this.linkRepository = linkRepository;
        this.linkFactory = linkFactory;
    }

    public CreateShortLinkResponse execute(CreateShortLinkRequest request) {

        User user = resolveUser(request.getUserId());

        if (linkRepository.findByUserIdAndOriginalUrl(user.getId(), request.getOriginalUrl()).isPresent()) {
            throw new IllegalArgumentException(
                    "Вы уже создавали короткую ссылку для этого URL." +
                            " Используйте меню редактирования, если хотите изменить параметры");
        }

        ShortLink link = linkFactory.create(
                user.getId(),
                request.getOriginalUrl(),
                request.getMaxClicks());

        linkRepository.save(link);

        return new CreateShortLinkResponse(
                user.getId(),
                "clck.ru/" + link.getShortCode());
    }

    private User resolveUser(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Необходимо выбрать пользователя");
        }

        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
    }
}
