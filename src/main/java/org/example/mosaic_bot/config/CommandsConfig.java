package org.example.mosaic_bot.config;

import org.example.mosaic_bot.codeGenerator.CodeGenerator;
import org.example.mosaic_bot.codeGenerator.HttpCodeGenerator;
import org.example.mosaic_bot.codeGenerator.InMemoryCodeGenerator;
import org.example.mosaic_bot.commands.*;
import org.example.mosaic_bot.dao.service.AdminService;
import org.example.mosaic_bot.dao.service.UserService;
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
    private final UserService userService;
    private final AdminService adminService;
    private final CodeGenerator codeGenerator;

    @Autowired
    public CommandsConfig(UserService userService, AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
        this.codeGenerator = new HttpCodeGenerator(baseUrlForCodeGeneratorService);
    }

    @Bean
    public List<Command> allCommands() {
        List<Command> availableCommands = new ArrayList<>();
        availableCommands.add(new StartCommand(userService));
        availableCommands.add(new LoginCommand(userService, adminService));
        availableCommands.add(new GenerateCodesCommand(userService, codeGenerator));
        Command helpCommand = new HelpCommand(userService, availableCommands.stream().map(Command::toApiCommand).collect(Collectors.toList()));
        availableCommands.add(helpCommand);
        return availableCommands;
    }
}
