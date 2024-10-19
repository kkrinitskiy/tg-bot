package com.kkrinitskiy.springbot.commands;

import com.kkrinitskiy.springbot.core.Resolver;
import com.kkrinitskiy.springbot.services.UserService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

public class TodoAppBotCommand implements AppBotCommand {

    UserService userService;
    Map<String, String> commands;

    public TodoAppBotCommand(UserService userService) {
        this.userService = userService;
        commands = new HashMap<>();
        commands.put("Да", "/todo_create");
        commands.put("Нет", "/todo_escape");

    }

    @Override
    public void handle(Update update, Resolver resolver) {
        if(!userService.findById(update.getMessage().getFrom().getId())){
            resolver.sendMenuButtons(update, "Создать список дел?", commands);
        }
    }
}
