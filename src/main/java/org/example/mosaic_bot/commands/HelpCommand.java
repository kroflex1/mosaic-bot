package org.example.mosaic_bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import org.example.mosaic_bot.dao.service.TelegramUserService;

import java.util.List;

public class HelpCommand extends Command {
    private final List<BotCommand> availableCommands;

    public HelpCommand(TelegramUserService telegramUserService, List<BotCommand> availableCommands) {
        super(telegramUserService);
        this.availableCommands = availableCommands;
    }

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "Вывести список всех команд";
    }

    @Override
    public AbstractSendRequest handle(Update update) {
        StringBuilder result = new StringBuilder("%s - %s".formatted(command(), description()));
        for (BotCommand command : availableCommands) {
            result.append("%s - %s".formatted(command.command(), command.description())).append("\n");
        }
        return new SendMessage(update.message().chat().id(), result.toString());
    }
}
