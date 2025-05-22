package org.example.ooppr.core.network.protocol;

import org.example.ooppr.core.users.User;

import java.util.List;

public class UserDisconnectedMessage extends Message {

    private final List<User> newUsersList;

    public UserDisconnectedMessage(List<User> newUsersList) {
        super(MessageType.USER_DISCONNECTED);
        this.newUsersList = newUsersList;
    }

    public List<User> getNewUsersList() {
        return this.newUsersList;
    }
}
