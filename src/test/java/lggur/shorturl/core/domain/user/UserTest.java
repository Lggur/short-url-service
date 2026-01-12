package lggur.shorturl.core.domain.user;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateUserWithId() {
        UUID id = UUID.randomUUID();
        User user = new User(id);

        assertNotNull(user);
        assertEquals(id, user.getId());
    }

    @Test
    void shouldCreateUserWithRandomIdWhenUsingConstructor() {
        UUID id = UUID.randomUUID();
        User user = new User(id);

        assertEquals(id, user.getId());
    }
}
