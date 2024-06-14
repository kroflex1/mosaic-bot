package org.example.mosaic_bot.exceptions;

public class AlreadyExistsAdminException extends RuntimeException {
    public AlreadyExistsAdminException(String name) {
        super("Admin with name %s already registered".formatted(name));
    }
}
