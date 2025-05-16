package org.example.ooppr.core.users;

import java.io.Serializable;

public class User implements Serializable {

    private final String nickname;
    private Role role;

    public enum Role {
        ADMIN,
        VIEW_ONLY,
        EDIT,
        CREATOR
    }

    public User(String nickname, Role role) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return this.nickname;
    }

    public Role getRole() {
        return role;
    }

    public void setRole( Role newRole ) {
        this.role = newRole;
    }

}
