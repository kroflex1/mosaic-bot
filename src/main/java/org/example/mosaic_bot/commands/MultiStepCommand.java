package org.example.mosaic_bot.commands;

import com.pengrad.telegrambot.model.Update;
import org.example.mosaic_bot.dao.dto.UserDTO;
import org.example.mosaic_bot.dao.entity.UserStatus;
import org.example.mosaic_bot.dao.service.UserService;

import java.util.Optional;
import java.util.Set;

public abstract class MultiStepCommand extends Command {
    public MultiStepCommand(UserService userService) {
        super(userService);
    }

    protected abstract Set<UserStatus> getSupportedStatuses();

    @Override
    public boolean isSupport(Update update) {
        Long chatId;
        if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().from().id();
        } else {
            chatId = update.message().chat().id();
        }
        Optional<UserDTO> userDTO = userService.getUserById(chatId);
        if (userDTO.isEmpty()) {
            userService.registerUser(chatId);
            userDTO = userService.getUserById(chatId);
        }
        if (userDTO.get().getStatus().equals(UserStatus.CHILLING) && update.message() != null && update.message().text().equals(command())) {
            return true;
        } else if (!userDTO.get().getStatus().equals(UserStatus.CHILLING) && update.message() != null && isCommand(update.message().text())) {
            return false;
        }
        return getSupportedStatuses().contains(userDTO.get().getStatus());
    }

    private boolean isCommand(String message) {
        return message.startsWith("/");
    }
}
