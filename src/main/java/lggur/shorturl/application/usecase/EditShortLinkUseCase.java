package lggur.shorturl.application.usecase;

import lggur.shorturl.application.dto.EditShortLinkRequest;
import lggur.shorturl.core.domain.link.ShortLink;
import lggur.shorturl.core.domain.link.ShortLinkRepository;

public class EditShortLinkUseCase {

    private final ShortLinkRepository linkRepository;
    private final int minClicks;
    private final int maxClicksLimit;

    public EditShortLinkUseCase(
            ShortLinkRepository linkRepository,
            int minClicks,
            int maxClicksLimit) {
        this.linkRepository = linkRepository;
        this.minClicks = minClicks;
        this.maxClicksLimit = maxClicksLimit;
    }

    public void execute(EditShortLinkRequest request) {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("Необходимо выбрать пользователя");
        }

        ShortLink link = linkRepository.findByShortCode(request.getShortCode())
                .orElseThrow(
                        () -> new IllegalArgumentException("Ссылка с кодом " + request.getShortCode() + " не найдена"));

        if (!link.isOwnedBy(request.getUserId())) {
            throw new SecurityException("У вас нет прав на редактирование этой ссылки");
        }

        if (request.getNewOriginalUrl() != null) {
            link.updateOriginalUrl(request.getNewOriginalUrl());
        }

        if (request.getNewMaxClicks() != null) {
            validateMaxClicks(request.getNewMaxClicks());
            link.updateMaxClicks(request.getNewMaxClicks());
        }

        linkRepository.save(link);
    }

    private void validateMaxClicks(int maxClicks) {
        if (maxClicks < minClicks || maxClicks > maxClicksLimit) {
            throw new IllegalArgumentException(
                    String.format("Лимит переходов должен быть в диапазоне от %d до %d", minClicks, maxClicksLimit));
        }
    }
}
