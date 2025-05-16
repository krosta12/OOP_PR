package org.example.ooppr.core.network.protocol;

import org.example.ooppr.core.users.User;

public class InitUserMessage extends Message {

    private final User user;

    public InitUserMessage(User user) {
        super( MessageType.INIT_USER );
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
