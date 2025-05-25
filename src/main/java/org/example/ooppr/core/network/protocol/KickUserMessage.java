package org.example.ooppr.core.network.protocol;

import org.example.ooppr.core.users.User;

public class KickUserMessage extends Message{

    private final User kickee;
    private final User kicker;

    public KickUserMessage(User kickee, User kicker) {
        super(MessageType.KICK_USER);
        this.kickee = kickee;
        this.kicker = kicker;
    }

    public User getKickee() {
        return this.kickee;
    }

    public User getKicker() {
        return this.kicker;
    }
}
