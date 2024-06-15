package org.example.mosaic_bot.config;

import org.example.mosaic_bot.commands.Command;
import org.example.mosaic_bot.commands.HelpCommand;
import org.example.mosaic_bot.commands.LoginCommand;
import org.example.mosaic_bot.commands.StartCommand;
import org.example.mosaic_bot.dao.service.AdminService;
import org.example.mosaic_bot.dao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class CommandsConfig {
    private final UserService userService;
    private final AdminService adminService;

    @Autowired
    public CommandsConfig(UserService userService, AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }

    @Bean
    public List<Command> allCommands() {
        List<Command> availableCommands = new ArrayList<>();
        availableCommands.add(new StartCommand(userService));
        availableCommands.add(new LoginCommand(userService, adminService));
        Command helpCommand = new HelpCommand(userService, availableCommands.stream().map(Command::toApiCommand).collect(Collectors.toList()));
        availableCommands.add(helpCommand);
        return availableCommands;
    }
}
