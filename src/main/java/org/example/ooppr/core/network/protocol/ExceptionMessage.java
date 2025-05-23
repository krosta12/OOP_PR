package org.example.ooppr.core.network.protocol;

public class ExceptionMessage extends Message {

    private final Exception e;

    public ExceptionMessage( Exception e ) {
        super( MessageType.EXCEPTION );
        this.e = e;
    }

    public Exception getException() {
        return this.e;
    }
}
