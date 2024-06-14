package org.example.mosaic_bot.controller;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.example.mosaic_bot.commands.Command;
import org.springframework.stereotype.Component;

import java.util.List;


public class UserMessageProcessor {
    protected final List<? extends Command> commands;

    public UserMessageProcessor(List<? extends Command> commands) {
        this.commands = commands;
    }

    public SendMessage process(Update update) {
        for (Command command : commands) {
            if (command.isSupport(update)) {
                return command.handle(update);
            }
        }
        return new SendMessage(
                update.message().chat().id(),
                "Неизвестная комада. Введите /help, чтобы получить список доступных команд"
        );
    }

    public List<? extends Command> getAvailableCommands() {
        return commands;
    }
}
