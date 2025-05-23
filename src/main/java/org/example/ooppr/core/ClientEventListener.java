package org.example.ooppr.core;

public interface ClientEventListener {
    void onKick();
    void onNicknameNotUnique();
    void onDisconnect();
}
