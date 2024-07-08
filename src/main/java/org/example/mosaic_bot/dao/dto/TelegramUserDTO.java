package org.example.mosaic_bot.dao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.mosaic_bot.dao.entity.TelegramUserStatus;

@Getter
@Setter
@AllArgsConstructor
public class TelegramUserDTO {
    private Long chatId;
    private TelegramUserStatus status;
    private Boolean isAdmin;
}
