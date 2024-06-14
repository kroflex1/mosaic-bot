package org.example.mosaic_bot.config;

import com.pengrad.telegrambot.TelegramBot;
import org.example.mosaic_bot.commands.Command;
import org.example.mosaic_bot.controller.UserMessageProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BotConfig {
    @Value("${app.telegram-token}")
    private String telegramToken;
    private final List<Command> availableCommands;

    public BotConfig(List<Command> availableCommands) {
        this.availableCommands = availableCommands;
    }

    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(telegramToken);
    }

    @Bean
    public UserMessageProcessor userMessageProcessor() {
        return new UserMessageProcessor(availableCommands);
    }
}
