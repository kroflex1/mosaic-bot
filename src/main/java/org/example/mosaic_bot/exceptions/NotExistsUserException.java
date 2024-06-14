package org.example.mosaic_bot.exceptions;

public class NotExistsUserException extends RuntimeException{
    public NotExistsUserException(Long chatId){
        super("Usr with id %d not exists".formatted(chatId));
    }
}
