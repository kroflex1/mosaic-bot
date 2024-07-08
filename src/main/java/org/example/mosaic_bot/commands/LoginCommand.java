package org.example.mosaic_bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import org.example.mosaic_bot.dao.entity.TelegramUserStatus;
import org.example.mosaic_bot.dao.service.TelegramUserService;
import org.example.mosaic_bot.util.Emoji;
import org.example.mosaic_bot.web.MosaicWeb;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class LoginCommand extends MultiStepCommand {
    private static final Set<TelegramUserStatus> SUPPORTED_STATUSES = Set.of(TelegramUserStatus.WRITE_LOGIN, TelegramUserStatus.WRITE_PASSWORD);
    private static final Map<Long, String> AUTH_PROCESS = new HashMap<>();
    private final MosaicWeb mosaicWeb;

    public LoginCommand(TelegramUserService telegramUserService, MosaicWeb mosaicWeb) {
        super(telegramUserService);
        this.mosaicWeb = mosaicWeb;
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
    public AbstractSendRequest handle(Update update) {
        Long chatId = getChatId(update);
        TelegramUserStatus userStatus = telegramUserService.getUserById(chatId).get().getStatus();
        switch (userStatus) {
            case CHILLING:
                telegramUserService.setUserStatus(chatId, TelegramUserStatus.WRITE_LOGIN);
                return new SendMessage(chatId, "*Введите логин:*").parseMode(ParseMode.Markdown);
            case WRITE_LOGIN:
                AUTH_PROCESS.put(chatId, update.message().text());
                telegramUserService.setUserStatus(chatId, TelegramUserStatus.WRITE_PASSWORD);
                return new SendMessage(chatId, "*Введите пароль:*").parseMode(ParseMode.Markdown);
            case WRITE_PASSWORD:
                String password = update.message().text();
                String name = AUTH_PROCESS.get(chatId);
                telegramUserService.setUserStatus(chatId, TelegramUserStatus.CHILLING);
                if (isValidAuthData(name, password)) {
                    telegramUserService.setAdminStatus(chatId, true);
                    return new SendMessage(chatId, "%sВы успешно вошли в аккаунт, теперь вы можете сгенерировать коды с помощью /generate_codes".formatted(Emoji.TICK.getCode()));
                }
                return new SendMessage(chatId, "%sЛоигн или пароль неверный, попробуйте войти ещё раз /login".formatted(Emoji.CROSS.getCode()));
        }
        return new SendMessage(chatId, "Что-то пошло не так, попробуйте использовать команду снова");
    }

    @Override
    protected Set<TelegramUserStatus> getSupportedStatuses() {
        return SUPPORTED_STATUSES;
    }

    private boolean isValidAuthData(String adminName, String adminPassword) {
        Optional<MosaicWeb.UserLoginInf> user = mosaicWeb.loginToService(adminName, adminPassword);
        if (user.isEmpty())
            return false;
        return user.get().getUser().getRole().equals("admin");
    }
}
