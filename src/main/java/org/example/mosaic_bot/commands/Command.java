package org.example.mosaic_bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.example.mosaic_bot.dao.service.UserService;

public abstract class Command {
    protected final UserService userService;

    public Command(UserService userService) {
        this.userService = userService;
    }

    public abstract String command();

    public abstract String description();

    public abstract SendMessage handle(Update update);

    public boolean isSupport(Update update) {
        return update.message() != null && update.message().text().equals(command());
    }

    public BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }

    protected Long getChatId(Update update) {
        Long chatId;
        if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().from().id();
        } else {
            chatId = update.message().chat().id();
        }
        return chatId;
    }
}
