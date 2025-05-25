package org.example.ooppr.core;

import org.example.ooppr.core.drawing.DrawAction;
import org.example.ooppr.core.network.protocol.DrawActionMessage;
import org.example.ooppr.core.users.User;

import java.util.List;

public interface ClientEventListener {
    void onKick();
    void onDisconnect();
    void onInitializeCanvas( int xRes, int yRes, String colorWeb, List<DrawAction> actions );
    void onDrawActionMessageReceived( DrawAction action, User sender );
    void onUndo();
    void onNewUsersList( List<User> usersList );
    void onException( Exception e );
}
