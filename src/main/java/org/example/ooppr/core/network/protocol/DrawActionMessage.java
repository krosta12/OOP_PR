package org.example.ooppr.core.network.protocol;

import org.example.ooppr.core.drawing.DrawAction;

import java.io.Serial;

public class DrawActionMessage extends Message {

    @Serial
    private static final long serialVersionUID = 1L;

    private final DrawAction action;
    private final String nickname;

    public DrawActionMessage(DrawAction action, String senderNickname) {
        super( MessageType.DRAW_ACTION );
        this.action = action;
        this.nickname = senderNickname;
    }

    public DrawAction getDrawAction() {
        return this.action;
    }

    public String getNickname() {
        return this.nickname;
    }
}
