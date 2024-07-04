package org.example.mosaic_bot.dao.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.mosaic_bot.dao.entity.TelegramUserStatus;

@Getter
@Setter
@AllArgsConstructor
public class TelegramUserDTO {
    private Long chatId;
    @Nullable
    private UserDTO admin;
    private TelegramUserStatus status;
}
