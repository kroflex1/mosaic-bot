package org.example.mosaic_bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import org.example.mosaic_bot.dao.dto.TelegramUserDTO;
import org.example.mosaic_bot.dao.service.TelegramUserService;
import org.example.mosaic_bot.util.Emoji;

import java.util.Optional;

public class StartCommand extends Command {
    public StartCommand(TelegramUserService telegramUserService) {
        super(telegramUserService);
    }

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "Начать работу с ботом";
    }

    @Override
    public AbstractSendRequest handle(Update update) {
        Long chatId = getChatId(update);
        Optional<TelegramUserDTO> userDTO = telegramUserService.getUserById(chatId);
        if (userDTO.isEmpty()) {
            telegramUserService.registerUser(chatId);
            return new SendMessage(chatId, "%sПривет, перед началом работы с ботом вы должны авторизоваться с помощью команды /login".formatted(Emoji.WAVING_HAND.getCode()));
        }
        return new SendMessage(chatId, "%sВы уже пользуетесь данным ботом, используйте /help, чтобы получить список доступных команд".formatted(Emoji.WAVING_HAND.getCode()));

    }
}
