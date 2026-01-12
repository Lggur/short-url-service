package lggur.shorturl.infra.service;

import lggur.shorturl.core.domain.link.ShortCodeGenerator;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class SimpleShortCodeGenerator implements ShortCodeGenerator {

    @Override
    public String generate(String originalUrl, String userId) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(
                    (originalUrl + userId).getBytes(StandardCharsets.UTF_8)
            );

            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(hash)
                    .substring(0, 6);

        } catch (Exception e) {
            throw new RuntimeException("Не удалось сгенерировать короткий код", e);
        }
    }
}
