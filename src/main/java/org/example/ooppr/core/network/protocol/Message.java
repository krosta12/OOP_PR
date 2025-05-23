package org.example.ooppr.core.network.protocol;

import java.io.Serializable;

public abstract class Message implements Serializable {
    private static long serialVersionID = 1L;

    public enum MessageType {
        DRAW_ACTION,
        CANVAS_STATE,
        UNDO,
        INIT_USER,
        USER_CONNECTED,
        USER_DISCONNECTED,
        USER_DISCONNECT,
        KICK_USER,
        CHAT // TODO do we need it?
    }

    private final MessageType type;

    protected Message(MessageType type) {
        this.type = type;
    }

    public MessageType getType() {
        return type;
    }

}
