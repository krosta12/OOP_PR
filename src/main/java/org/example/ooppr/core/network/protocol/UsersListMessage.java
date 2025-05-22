package org.example.ooppr.core.network.protocol;

import org.example.ooppr.core.users.User;

import java.util.List;

public class UsersListMessage extends Message {

    private final List<User> connectedUsers;

    public UsersListMessage(List<User> connectedUsers) {
        super(MessageType.USERS_LIST);
        this.connectedUsers = connectedUsers;

    }

    public List<User> getConnectedUsers() {
        return this.connectedUsers;
    }
}
