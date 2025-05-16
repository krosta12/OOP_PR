package org.example.ooppr.core.users;

public class User {

    private final String nickname;

    public User(String nickname) {
        this.nickname = nickname;
    }

    private enum role {
        ADMIN,
        VIEW_ONLY,
        EDIT
    }
    public String getNickname() {
        return this.nickname;
    }

}
