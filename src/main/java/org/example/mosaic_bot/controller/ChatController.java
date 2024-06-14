package org.example.mosaic_bot.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ChatController implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final UserMessageProcessor userMessageProcessor;

    @Autowired
    public ChatController(TelegramBot telegramBot, UserMessageProcessor userMessageProcessor) {
        this.telegramBot = telegramBot;
        this.telegramBot.setUpdatesListener(this);
        BotCommand[] botCommands = new BotCommand[userMessageProcessor.getAvailableCommands().size()];
        for (int i = 0; i < botCommands.length; i++) {
            botCommands[i] = userMessageProcessor.getAvailableCommands().get(i).toApiCommand();
        }
        this.telegramBot.execute(new SetMyCommands(botCommands));
        this.userMessageProcessor = userMessageProcessor;
    }

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            SendMessage sendMessage = userMessageProcessor.process(update);
            execute(sendMessage);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        telegramBot.execute(request);
    }
}
