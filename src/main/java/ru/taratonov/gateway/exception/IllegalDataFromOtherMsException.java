package ru.taratonov.gateway.exception;

public class IllegalDataFromOtherMsException extends RuntimeException {
    public IllegalDataFromOtherMsException(String message) {
        super(message);
    }
}
