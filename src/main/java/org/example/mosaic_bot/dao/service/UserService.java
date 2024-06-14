package org.example.mosaic_bot.dao.service;

import org.example.mosaic_bot.dao.dto.UserDTO;
import org.example.mosaic_bot.dao.entity.Admin;
import org.example.mosaic_bot.dao.entity.UserStatus;
import org.example.mosaic_bot.exceptions.NotExistsUserException;
import org.springframework.transaction.NoTransactionException;

import java.util.Optional;

public interface UserService {
    UserDTO registerUser(Long chatId);

    Optional<UserDTO> getUserById(Long chatId);

    UserStatus getUserStatus(Long chatId);

    UserDTO setUserStatus(Long chatId, UserStatus userStatus);

    UserDTO setUserAdminRole(Long chatId, String adminName, String adminPassword) throws NotExistsUserException;
}
