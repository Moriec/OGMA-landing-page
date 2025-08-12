package ru.ogma.exceptions;

public class PersonJsonDecodingException extends RuntimeException {
    public PersonJsonDecodingException(String message) {
        super(message);
    }
}
