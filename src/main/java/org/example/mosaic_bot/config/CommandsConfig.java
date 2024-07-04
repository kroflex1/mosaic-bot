package org.example.mosaic_bot.config;

import org.example.mosaic_bot.web.CodeGenerator;
import org.example.mosaic_bot.web.HttpCodeGenerator;
import org.example.mosaic_bot.commands.*;
import org.example.mosaic_bot.dao.service.AdminService;
import org.example.mosaic_bot.dao.service.TelegramUserService;
import org.example.mosaic_bot.web.MosaicWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class CommandsConfig {
    @Value("${app.code-generator-url}")
    private String baseUrlForCodeGeneratorService;
    @Value("${app.admin-login}")
    private String adminUsername;
    @Value("${app.admin-password}")
    private String adminPassword;
    private final TelegramUserService telegramUserService;
    private final MosaicWeb mosaicWeb;

    @Autowired
    public CommandsConfig(TelegramUserService telegramUserService) {
        this.telegramUserService = telegramUserService;
        this.mosaicWeb = new MosaicWeb(baseUrlForCodeGeneratorService, adminUsername, adminPassword);
    }

    @Bean
    public List<Command> allCommands() {
        List<Command> availableCommands = new ArrayList<>();
        availableCommands.add(new StartCommand(telegramUserService));
        availableCommands.add(new LoginCommand(telegramUserService));
        availableCommands.add(new GenerateCodesCommand(telegramUserService, mosaicWeb));
        Command helpCommand = new HelpCommand(telegramUserService, availableCommands.stream().map(Command::toApiCommand).collect(Collectors.toList()));
        availableCommands.add(helpCommand);
        return availableCommands;
    }
}
