package org.example.mosaic_bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import org.example.mosaic_bot.codeGenerator.CodeGenerator;
import org.example.mosaic_bot.dao.dto.UserDTO;
import org.example.mosaic_bot.dao.entity.UserStatus;
import org.example.mosaic_bot.dao.service.UserService;
import org.example.mosaic_bot.util.Emoji;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GenerateCodesCommand extends MultiStepCommand {
    private static final Set<UserStatus> SUPPORTED_STATUSES = Set.of(UserStatus.WRITE_NUMBER_OF_CODES, UserStatus.WRITE_NUMBER_OF_REUSE);
    private static final Map<Long, Integer> CODES_GENERATE_PROCESS = new HashMap<>();
    private final CodeGenerator codeGenerator;

    public GenerateCodesCommand(UserService userService, CodeGenerator codeGenerator) {
        super(userService);
        this.codeGenerator = codeGenerator;
    }

    @Override
    public String command() {
        return "/generate_codes";
    }

    @Override
    public String description() {
        return "Сгенерировать коды";
    }

    @Override
    public AbstractSendRequest handle(Update update) {
        Long chatId = getChatId(update);
        UserDTO userDTO = userService.getUserById(chatId).get();
        UserStatus userStatus = userDTO.getStatus();
        switch (userStatus) {
            case CHILLING:
                if (userDTO.getAdmin() == null) {
                    return new SendMessage(chatId, "%sВы должны войти в аккаунт с помощью /login, чтобы получить возможность генерировать коды".formatted(Emoji.EXCLAMATION_MARK.getCode()));
                }
                userService.setUserStatus(chatId, UserStatus.WRITE_NUMBER_OF_CODES);
                return new SendMessage(chatId, "*Введите количество кодов, которые вы хотите сгенерировать:*").parseMode(ParseMode.Markdown);
            case WRITE_NUMBER_OF_CODES:
                Integer numberOfCodes;
                try {
                    numberOfCodes = Integer.parseInt(update.message().text());
                } catch (NumberFormatException e) {
                    return new SendMessage(chatId, "%sВы можете вводить только числа, попробуйте ввести количество кодов ещё раз".formatted(Emoji.EXCLAMATION_MARK.getCode()));
                }
                if (numberOfCodes < 0) {
                    return new SendMessage(chatId, "%sЧисло кодов должно быть больше 0, попробуйте ввести количество кодов ещё раз".formatted(Emoji.EXCLAMATION_MARK.getCode()));
                }
                CODES_GENERATE_PROCESS.put(chatId, numberOfCodes);
                userService.setUserStatus(chatId, UserStatus.WRITE_NUMBER_OF_REUSE);
                return new SendMessage(chatId, "*Введите количество переиспользований для кода:*").parseMode(ParseMode.Markdown);
            case WRITE_NUMBER_OF_REUSE:
                Integer numberOfReuse;
                try {
                    numberOfReuse = Integer.parseInt(update.message().text());
                } catch (NumberFormatException e) {
                    return new SendMessage(chatId, "%sВы можете вводить только числа, попробуйте ввести количество переиспользований ещё раз".formatted(Emoji.EXCLAMATION_MARK.getCode()));
                }
                if (numberOfReuse < 0) {
                    return new SendMessage(chatId, "%sЧисло кодов должно быть больше 0, попробуйте ввести количество переиспользований ещё раз".formatted(Emoji.EXCLAMATION_MARK.getCode()));
                }
                userService.setUserStatus(chatId, UserStatus.CHILLING);
                File file = codeGenerator.generateFileWithCodes(CODES_GENERATE_PROCESS.get(chatId), numberOfReuse);
                return new SendDocument(chatId, file);

        }
        return new SendMessage(chatId, "Что-то пошло не так, попробуйте использовать команду снова");
    }

    @Override
    protected Set<UserStatus> getSupportedStatuses() {
        return SUPPORTED_STATUSES;
    }

}
