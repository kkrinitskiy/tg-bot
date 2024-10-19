package com.kkrinitskiy.springbot.commands;

import com.kkrinitskiy.springbot.core.Resolver;
import com.kkrinitskiy.springbot.services.UserService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

public class AdminAppBotCommand implements AppBotCommand {
    private Map<String, String> adminCommands;

    public AdminAppBotCommand(Map<String, String> adminCommands) {
        this.adminCommands = adminCommands;
    }

    @Override
    public void handle(Update update, Resolver resolver) {
        resolver.sendMenuButtons(update, "Админка", adminCommands);
    }
}
