package org.example.mosaic_bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import org.example.mosaic_bot.dao.dto.TelegramUserDTO;
import org.example.mosaic_bot.dao.entity.TelegramUserStatus;
import org.example.mosaic_bot.dao.service.TelegramUserService;
import org.example.mosaic_bot.util.Emoji;
import org.example.mosaic_bot.web.MosaicWeb;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GenerateCodesCommand extends MultiStepCommand {
    private static final Set<TelegramUserStatus> SUPPORTED_STATUSES = Set.of(TelegramUserStatus.WRITE_NUMBER_OF_CODES, TelegramUserStatus.WRITE_NUMBER_OF_REUSE);
    private static final Map<Long, Integer> CODES_GENERATE_PROCESS = new HashMap<>();
    private final MosaicWeb mosaicWeb;

    public GenerateCodesCommand(TelegramUserService telegramUserService, MosaicWeb mosaicWeb) {
        super(telegramUserService);
        this.mosaicWeb = mosaicWeb;
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
        TelegramUserDTO telegramUserDTO = telegramUserService.getUserById(chatId).get();
        TelegramUserStatus telegramUserStatus = telegramUserDTO.getStatus();
        switch (telegramUserStatus) {
            case CHILLING:
                if (telegramUserDTO.getIsAdmin() == false) {
                    return new SendMessage(chatId, "%sВы должны войти в аккаунт с помощью /login, чтобы получить возможность генерировать коды".formatted(Emoji.EXCLAMATION_MARK.getCode()));
                }
                telegramUserService.setUserStatus(chatId, TelegramUserStatus.WRITE_NUMBER_OF_CODES);
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
                telegramUserService.setUserStatus(chatId, TelegramUserStatus.WRITE_NUMBER_OF_REUSE);
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
                telegramUserService.setUserStatus(chatId, TelegramUserStatus.CHILLING);
                File file = mosaicWeb.generateFileWithCodes(CODES_GENERATE_PROCESS.get(chatId), numberOfReuse);
                return new SendDocument(chatId, file);

        }
        return new SendMessage(chatId, "Что-то пошло не так, попробуйте использовать команду снова");
    }

    @Override
    protected Set<TelegramUserStatus> getSupportedStatuses() {
        return SUPPORTED_STATUSES;
    }

}
