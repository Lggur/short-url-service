package lggur.shorturl.core.domain.link;

public interface ShortCodeGenerator {
    String generate(String originalUrl, String userId);
}
