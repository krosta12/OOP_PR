package org.example.ooppr.core.network.protocol;

import java.io.Serializable;

public class NotUniqueNicknameException extends Exception implements Serializable {
    public NotUniqueNicknameException(String message) {
        super(message);
    }
}
