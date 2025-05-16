package org.example.ooppr.core.network.protocol;

import java.io.Serial;

public class UndoMessage extends Message {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String nickname;

    public UndoMessage(String nickname) {
        super( MessageType.UNDO );
        this.nickname = nickname;
    }

    public String getNickname() {
        return this.nickname;
    }
}
