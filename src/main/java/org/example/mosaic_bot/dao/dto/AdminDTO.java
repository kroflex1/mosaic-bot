package org.example.mosaic_bot.dao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminDTO {
    private Long id;
    private String name;
    private String password;
}
