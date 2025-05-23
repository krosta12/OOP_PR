package org.example.ooppr.core.network.protocol;

import org.example.ooppr.core.drawing.DrawAction;
import org.example.ooppr.core.users.User;

import java.io.Serial;

public class DrawActionMessage extends Message {

    @Serial
    private static final long serialVersionUID = 1L;

    private final DrawAction action;
    private final User sender;

    public DrawActionMessage(DrawAction action, User sender) {
        super( MessageType.DRAW_ACTION );
        this.action = action;
        this.sender = sender;
    }

    public DrawAction getDrawAction() {
        return this.action;
    }

    public User getSender() {
        return this.sender;
    }
}
