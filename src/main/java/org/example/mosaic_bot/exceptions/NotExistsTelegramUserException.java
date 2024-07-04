package org.example.mosaic_bot.exceptions;

public class NotExistsTelegramUserException extends RuntimeException{
    public NotExistsTelegramUserException(Long chatId){
        super("Telegram user with id %d not exists".formatted(chatId));
    }
}
