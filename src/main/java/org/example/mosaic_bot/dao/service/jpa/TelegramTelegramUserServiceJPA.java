package org.example.mosaic_bot.dao.service.jpa;

import org.example.mosaic_bot.dao.dto.TelegramUserDTO;
import org.example.mosaic_bot.dao.entity.Admin;
import org.example.mosaic_bot.dao.entity.TelegramUser;
import org.example.mosaic_bot.dao.entity.TelegramUserStatus;
import org.example.mosaic_bot.dao.entity.User;
import org.example.mosaic_bot.dao.repository.AdminRepository;
import org.example.mosaic_bot.dao.repository.TelegramUserRepository;
import org.example.mosaic_bot.dao.repository.UserRepository;
import org.example.mosaic_bot.dao.service.TelegramUserService;
import org.example.mosaic_bot.exceptions.NotExistsUserException;
import org.example.mosaic_bot.exceptions.NotExistsTelegramUserException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class TelegramTelegramUserServiceJPA implements TelegramUserService {
    private final TelegramUserRepository telegramUserRepository;
    private final UserRepository userRepository;

    public TelegramTelegramUserServiceJPA(TelegramUserRepository telegramUserRepository,
                                          UserRepository userRepository) {
        this.telegramUserRepository = telegramUserRepository;
        this.userRepository = userRepository;
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
    public TelegramUserDTO setUserAdminRole(Long chatId, UUID adminId) throws NotExistsTelegramUserException {
        Optional<TelegramUser> user = telegramUserRepository.findById(chatId);
        Optional<User> admin = userRepository.findById(adminId);
        if (user.isEmpty()) {
            throw new NotExistsTelegramUserException(chatId);
        }
        if (admin.isEmpty()) {
            throw new NotExistsUserException(chatId);
        }
        user.get().setAdmin(admin.get());
        telegramUserRepository.save(user.get());
        return user.get().convertToDTO();
    }

}
