package org.example.mosaic_bot.exceptions;

public class NotExistsAdminException extends RuntimeException{
    public NotExistsAdminException(Long adminId){
        super("Admin with id %d not exists".formatted(adminId));
    }
}
