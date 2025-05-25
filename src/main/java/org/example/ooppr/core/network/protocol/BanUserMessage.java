package org.example.ooppr.core.network.protocol;

import org.example.ooppr.core.users.User;

public class BanUserMessage extends Message{

    private final User banned;
    private final User banner;

    public BanUserMessage(User banned, User banner ) {
        super(MessageType.BAN_USER);
        this.banned = banned;
        this.banner = banner;
    }

    public User getBanned() {
        return this.banned;
    }

    public User getBanner() {
        return this.banner;
    }
}
