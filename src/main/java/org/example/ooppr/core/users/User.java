package org.example.ooppr.core.users;

import java.io.Serializable;
import java.time.LocalDateTime;

public class User implements Serializable {

    private final String nickname;
    private Role role;
    private LocalDateTime connectionTime;

    public enum Role {
        ADMIN("Admin", 1),
        VIEW_ONLY("View only", 3),
        EDIT("Edit", 2),
        CREATOR("Creator", 0);

        private final String displayName;
        private final int priority;

        Role(String displayName, int rolePriority) {
            this.displayName = displayName;
            this.priority = rolePriority;
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

    public int getRolePriority() {
        return role.priority;
    }

    public LocalDateTime getConnectionTime() {
        return this.connectionTime;
    }

    public void setRole( Role newRole ) {
        this.role = newRole;
    }

}
