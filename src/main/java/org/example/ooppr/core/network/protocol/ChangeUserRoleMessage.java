package org.example.ooppr.core.network.protocol;

import org.example.ooppr.core.users.User;

public class ChangeUserRoleMessage extends Message{

    private final User user;
    private final User.Role newRole;

    public ChangeUserRoleMessage(User user, User.Role newRole ) {
        super( MessageType.CHANGE_USER_ROLE );
        this.user = user;
        this.newRole = newRole;
    }

    public User getUser() {
        return this.user;
    }

    public User.Role getNewRole() {
        return this.newRole;
    }
}
