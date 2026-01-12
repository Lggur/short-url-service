package lggur.shorturl.application.usecase;

import lggur.shorturl.application.dto.DeleteShortLinkRequest;
import lggur.shorturl.core.domain.link.ShortLink;
import lggur.shorturl.core.domain.link.ShortLinkRepository;

public class DeleteShortLinkUseCase {

    private final ShortLinkRepository linkRepository;

    public DeleteShortLinkUseCase(ShortLinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public void execute(DeleteShortLinkRequest request) {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("Необходимо выбрать пользователя");
        }

        ShortLink link = linkRepository.findByShortCode(request.getShortCode())
                .orElseThrow(
                        () -> new IllegalArgumentException("Ссылка с кодом " + request.getShortCode() + " не найдена"));

        if (!link.isOwnedBy(request.getUserId())) {
            throw new SecurityException("У вас нет прав на удаление этой ссылки");
        }

        linkRepository.delete(link);
    }
}
