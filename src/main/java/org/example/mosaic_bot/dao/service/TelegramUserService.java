package org.example.mosaic_bot.dao.service;

import org.example.mosaic_bot.dao.dto.TelegramUserDTO;
import org.example.mosaic_bot.dao.entity.TelegramUserStatus;
import org.example.mosaic_bot.exceptions.NotExistsTelegramUserException;

import java.util.Optional;
import java.util.UUID;

public interface TelegramUserService {
    TelegramUserDTO registerUser(Long chatId);

    Optional<TelegramUserDTO> getUserById(Long chatId);

    TelegramUserStatus getUserStatus(Long chatId);

    TelegramUserDTO setUserStatus(Long chatId, TelegramUserStatus telegramUserStatus);

    TelegramUserDTO setUserAdminRole(Long chatId, UUID adminId) throws NotExistsTelegramUserException;
}
