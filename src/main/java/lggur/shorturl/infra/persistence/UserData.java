package lggur.shorturl.infra.persistence;

import lggur.shorturl.core.domain.user.User;

import java.util.UUID;

public class UserData {

    private String id;

    public UserData() { }

    public UserData(String id) {
        this.id = id;
    }

    public static UserData fromUser(User user) {
        return new UserData(user.getId().toString());
    }

    public User toUser() {
        return new User(UUID.fromString(id));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
