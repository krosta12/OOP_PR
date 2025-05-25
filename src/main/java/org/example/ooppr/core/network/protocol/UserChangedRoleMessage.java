package org.example.ooppr.core.network.protocol;

import org.example.ooppr.core.users.User;

import java.util.List;

public class UserChangedRoleMessage extends Message{

    private final List<User> newUsersList;

    public UserChangedRoleMessage( List<User> newUsersList ) {
        super(Message.MessageType.CHANGE_USER_ROLE);
        this.newUsersList = newUsersList;
    }

    public List<User> getNewUsersList() {
        return this.newUsersList;
    }

}
