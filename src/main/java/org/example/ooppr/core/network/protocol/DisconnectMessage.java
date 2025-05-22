package org.example.ooppr.core.network.protocol;

import org.example.ooppr.core.users.User;

public class DisconnectMessage extends Message {

    private final User user;

    public DisconnectMessage(User user) {
        super(MessageType.USER_DISCONNECT);
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
