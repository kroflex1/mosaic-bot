package org.example.mosaic_bot.dao.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.mosaic_bot.dao.entity.UserStatus;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private Long chatId;
    @Nullable
    private AdminDTO admin;
    private UserStatus status;
}
