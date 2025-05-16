package org.example.ooppr.core.users;

import java.io.Serializable;
import java.time.LocalDateTime;

public class User implements Serializable {

    private final String nickname;
    private Role role;
    private LocalDateTime connectionTime;

    public enum Role {
        ADMIN("Admin"),
        VIEW_ONLY("View only"),
        EDIT("Edit"),
        CREATOR("Creator");

        private final String displayName;

        Role(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public User( String nickname, Role role, LocalDateTime connectionTime ) {
        this.nickname = nickname;
        this.role = role;
        this.connectionTime = connectionTime;
    }

    public String getNickname() {
        return this.nickname;
    }

    public Role getRole() {
        return role;
    }

    public LocalDateTime getConnectionTime() {
        return this.connectionTime;
    }

    public void setRole( Role newRole ) {
        this.role = newRole;
    }

}
