package org.example.mosaic_bot.exceptions;

public class NotExistsUserException extends RuntimeException{
    public NotExistsUserException(Long adminId){
        super("Admin with id %d not exists".formatted(adminId));
    }
}
