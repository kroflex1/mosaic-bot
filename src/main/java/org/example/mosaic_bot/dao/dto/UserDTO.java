package org.example.mosaic_bot.dao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private UUID id;
    private String username;
    private String hashedPassword;
}
