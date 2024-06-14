package org.example.mosaic_bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.example.mosaic_bot.dao.dto.AdminDTO;
import org.example.mosaic_bot.dao.entity.UserStatus;
import org.example.mosaic_bot.dao.service.AdminService;
import org.example.mosaic_bot.dao.service.UserService;
import org.example.mosaic_bot.util.Emoji;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class LoginCommand extends MultiStepCommand {
    private static final Set<UserStatus> SUPPORTED_STATUSES = Set.of(UserStatus.WRITE_LOGIN, UserStatus.WRITE_PASSWORD);
    private static final Map<Long, String> AUTH_PROCESS = new HashMap<>();
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;

    public LoginCommand(UserService userService, AdminService adminService, PasswordEncoder passwordEncoder) {
        super(userService);
        this.adminService = adminService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String command() {
        return "/login";
    }

    @Override
    public String description() {
        return "Войти в аккаунт";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = getChatId(update);
        UserStatus userStatus = userService.getUserById(chatId).get().getStatus();
        switch (userStatus) {
            case CHILLING:
                userService.setUserStatus(chatId, UserStatus.WRITE_LOGIN);
                return new SendMessage(chatId, "*Введите логин:*").parseMode(ParseMode.Markdown);
            case WRITE_LOGIN:
                AUTH_PROCESS.put(chatId, update.message().text());
                userService.setUserStatus(chatId, UserStatus.WRITE_PASSWORD);
                return new SendMessage(chatId, "*Введите пароль:*").parseMode(ParseMode.Markdown);
            case WRITE_PASSWORD:
                String password = update.message().text();
                String name = AUTH_PROCESS.get(chatId);
                userService.setUserStatus(chatId, UserStatus.CHILLING);
                if (isValidAuthData(name, password)) {
                    userService.setUserAdminRole(chatId, name, password);
                    return new SendMessage(chatId, "%sВы успешно вошли в аккаунт, теперь вы можете сгенерировать коды с помощью /generate_codes".formatted(Emoji.TICK.getCode()));
                }
                return new SendMessage(chatId, "%sЛоигн или пароль неверный, попробуйте войти ещё раз /login".formatted(Emoji.CROSS.getCode()));
        }
        return new SendMessage(chatId, "Что-то пошло не так, попробуйте использовать команду снова");
    }

    @Override
    protected Set<UserStatus> getSupportedStatuses() {
        return SUPPORTED_STATUSES;
    }

    private boolean isValidAuthData(String adminName, String adminPassword) {
        String hashedPassword = passwordEncoder.encode(adminPassword);
        Optional<AdminDTO> admin = adminService.getAdminByNameAndPassword(adminName, hashedPassword);
        return admin.isPresent();
    }
}
