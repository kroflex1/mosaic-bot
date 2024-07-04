package org.example.mosaic_bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import org.example.mosaic_bot.dao.service.TelegramUserService;

public abstract class Command {
    protected final TelegramUserService telegramUserService;

    public Command(TelegramUserService telegramUserService) {
        this.telegramUserService = telegramUserService;
    }

    public abstract String command();

    public abstract String description();

    public abstract AbstractSendRequest handle(Update update);

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
