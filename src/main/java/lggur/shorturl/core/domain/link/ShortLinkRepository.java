package lggur.shorturl.core.domain.link;

import java.util.Optional;

public interface ShortLinkRepository {

    void save(ShortLink link);

    Optional<ShortLink> findByShortCode(String shortCode);

    void delete(ShortLink link);

    Optional<ShortLink> findByUserIdAndOriginalUrl(java.util.UUID userId, String originalUrl);
}
