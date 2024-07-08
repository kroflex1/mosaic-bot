package org.example.mosaic_bot.dao.service.jpa;

import org.example.mosaic_bot.dao.dto.TelegramUserDTO;
import org.example.mosaic_bot.dao.entity.TelegramUser;
import org.example.mosaic_bot.dao.entity.TelegramUserStatus;
import org.example.mosaic_bot.dao.repository.TelegramUserRepository;
import org.example.mosaic_bot.dao.service.TelegramUserService;
import org.example.mosaic_bot.exceptions.NotExistsTelegramUserException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TelegramTelegramUserServiceJPA implements TelegramUserService {
    private final TelegramUserRepository telegramUserRepository;


    public TelegramTelegramUserServiceJPA(TelegramUserRepository telegramUserRepository) {
        this.telegramUserRepository = telegramUserRepository;
    }

    @Override
    public TelegramUserDTO registerUser(Long chatId) {
        TelegramUser telegramUser = new TelegramUser(chatId);
        telegramUserRepository.save(telegramUser);
        return telegramUser.convertToDTO();
    }

    @Override
    public Optional<TelegramUserDTO> getUserById(Long chatId) {
        Optional<TelegramUser> user = telegramUserRepository.findById(chatId);
        return user.map(TelegramUser::convertToDTO);
    }

    @Override
    public TelegramUserStatus getUserStatus(Long chatId) throws NotExistsTelegramUserException {
        Optional<TelegramUser> user = telegramUserRepository.findById(chatId);
        if (user.isEmpty()) {
            throw new NotExistsTelegramUserException(chatId);
        }
        return user.get().getStatus();
    }

    @Override
    public TelegramUserDTO setUserStatus(Long chatId, TelegramUserStatus telegramUserStatus) throws NotExistsTelegramUserException {
        Optional<TelegramUser> user = telegramUserRepository.findById(chatId);
        if (user.isEmpty()) {
            throw new NotExistsTelegramUserException(chatId);
        }
        user.get().setStatus(telegramUserStatus);
        telegramUserRepository.save(user.get());
        return user.get().convertToDTO();
    }

    @Override
    public TelegramUserDTO setAdminStatus(Long chatId, Boolean isAdmin) {
        Optional<TelegramUser> user = telegramUserRepository.findById(chatId);
        if (user.isEmpty()) {
            throw new NotExistsTelegramUserException(chatId);
        }
        user.get().setIsAdmin(isAdmin);
        telegramUserRepository.save(user.get());
        return user.get().convertToDTO();
    }
}
