package org.example.mosaic_bot.commands;

import com.pengrad.telegrambot.model.Update;
import org.example.mosaic_bot.dao.dto.TelegramUserDTO;
import org.example.mosaic_bot.dao.entity.TelegramUserStatus;
import org.example.mosaic_bot.dao.service.TelegramUserService;

import java.util.Optional;
import java.util.Set;

public abstract class MultiStepCommand extends Command {
    public MultiStepCommand(TelegramUserService telegramUserService) {
        super(telegramUserService);
    }

    protected abstract Set<TelegramUserStatus> getSupportedStatuses();

    @Override
    public boolean isSupport(Update update) {
        Long chatId;
        if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().from().id();
        } else {
            chatId = update.message().chat().id();
        }
        Optional<TelegramUserDTO> userDTO = telegramUserService.getUserById(chatId);
        if (userDTO.isEmpty()) {
            telegramUserService.registerUser(chatId);
            userDTO = telegramUserService.getUserById(chatId);
        }
        if (userDTO.get().getStatus().equals(TelegramUserStatus.CHILLING) && update.message() != null && update.message().text().equals(command())) {
            return true;
        } else if (!userDTO.get().getStatus().equals(TelegramUserStatus.CHILLING) && update.message() != null && isCommand(update.message().text())) {
            telegramUserService.setUserStatus(chatId, TelegramUserStatus.CHILLING);
            return false;
        }
        return getSupportedStatuses().contains(userDTO.get().getStatus());
    }

    private boolean isCommand(String message) {
        return message.startsWith("/");
    }
}
