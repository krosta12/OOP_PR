package org.example.ooppr.core.network.protocol;

import org.example.ooppr.core.users.User;

import java.util.List;

public class UserConnectedMessage extends Message {

    private final List<User> newUsersList;

    public UserConnectedMessage(List<User> newUsersList) {
        super(MessageType.USER_CONNECTED);
        this.newUsersList = newUsersList;
    }

    public List<User> getNewUsersList() {
        return this.newUsersList;
    }
}
