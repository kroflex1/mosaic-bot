package org.example.mosaic_bot.dao.service.jpa;

import org.example.mosaic_bot.dao.dto.UserDTO;
import org.example.mosaic_bot.dao.entity.Admin;
import org.example.mosaic_bot.dao.entity.User;
import org.example.mosaic_bot.dao.entity.UserStatus;
import org.example.mosaic_bot.dao.repository.AdminRepository;
import org.example.mosaic_bot.dao.repository.UserRepository;
import org.example.mosaic_bot.dao.service.UserService;
import org.example.mosaic_bot.exceptions.NotExistsAdminException;
import org.example.mosaic_bot.exceptions.NotExistsUserException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceJPA implements UserService {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public UserServiceJPA(UserRepository userRepository, AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDTO registerUser(Long chatId) {
        User user = new User(chatId);
        userRepository.save(user);
        return user.convertToDTO();
    }

    @Override
    public Optional<UserDTO> getUserById(Long chatId) {
        Optional<User> user = userRepository.findById(chatId);
        return user.map(User::convertToDTO);
    }

    @Override
    public UserStatus getUserStatus(Long chatId) throws NotExistsUserException {
        Optional<User> user = userRepository.findById(chatId);
        if (user.isEmpty()) {
            throw new NotExistsUserException(chatId);
        }
        return user.get().getStatus();
    }

    @Override
    public UserDTO setUserStatus(Long chatId, UserStatus userStatus) throws NotExistsUserException {
        Optional<User> user = userRepository.findById(chatId);
        if (user.isEmpty()) {
            throw new NotExistsUserException(chatId);
        }
        user.get().setStatus(userStatus);
        userRepository.save(user.get());
        return user.get().convertToDTO();
    }

    @Override
    public UserDTO setUserAdminRole(Long chatId, String adminName) throws NotExistsUserException {
        Optional<User> user = userRepository.findById(chatId);
        List<Admin> admin = adminRepository.findByNameIs(adminName);
        if (user.isEmpty()) {
            throw new NotExistsUserException(chatId);
        }
        if (admin.isEmpty()) {
            throw new NotExistsAdminException(chatId);
        }
        user.get().setAdmin(admin.get(0));
        userRepository.save(user.get());
        return user.get().convertToDTO();
    }

}
