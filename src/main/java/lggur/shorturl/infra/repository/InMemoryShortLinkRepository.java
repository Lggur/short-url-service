package lggur.shorturl.infra.repository;

import lggur.shorturl.core.domain.link.ShortLink;
import lggur.shorturl.core.domain.link.ShortLinkRepository;

import java.util.*;

public class InMemoryShortLinkRepository implements ShortLinkRepository {

    private final Map<String, ShortLink> byCode = new HashMap<>();

    @Override
    public void save(ShortLink link) {
        byCode.put(link.getShortCode(), link);
    }

    @Override
    public Optional<ShortLink> findByShortCode(String shortCode) {
        return Optional.ofNullable(byCode.get(shortCode));
    }

    @Override
    public void delete(ShortLink link) {
        byCode.remove(link.getShortCode());
    }

    public List<ShortLink> findAll() {
        return new ArrayList<>(byCode.values());
    }

    public void saveAll(List<ShortLink> links) {
        for (ShortLink link : links) {
            byCode.put(link.getShortCode(), link);
        }
    }

    public void clear() {
        byCode.clear();
    }

    @Override
    public Optional<ShortLink> findByUserIdAndOriginalUrl(UUID userId, String originalUrl) {
        return byCode.values().stream()
                .filter(link -> link.getOwnerId().equals(userId) && link.getOriginalUrl().equals(originalUrl))
                .findFirst();
    }
}
