package lggur.shorturl.infra.service;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class InfraServicesTest {

    @Test
    void shortCodeGeneratorShouldProduceConsistentCodes() {
        SimpleShortCodeGenerator generator = new SimpleShortCodeGenerator();
        String url = "https://google.com";
        String user = UUID.randomUUID().toString();

        String code1 = generator.generate(url, user);
        String code2 = generator.generate(url, user);

        assertEquals(code1, code2);
        assertEquals(6, code1.length());
    }

    @Test
    void fixedLifetimePolicyShouldCalculateCorrectly() {
        Duration lifetime = Duration.ofHours(24);
        FixedLifetimePolicy policy = new FixedLifetimePolicy(lifetime);
        Instant now = Instant.now();

        Instant expiresAt = policy.calculateExpiration(now);

        assertEquals(now.plus(Duration.ofHours(24)), expiresAt);
    }
}
