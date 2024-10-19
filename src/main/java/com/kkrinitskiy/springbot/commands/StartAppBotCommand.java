package com.kkrinitskiy.springbot.commands;

import com.kkrinitskiy.springbot.core.Resolver;
import com.kkrinitskiy.springbot.models.User;
import com.kkrinitskiy.springbot.services.UserService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartAppBotCommand implements AppBotCommand {
    private UserService userService;

    public StartAppBotCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(Update update, Resolver resolver) {
        org.telegram.telegrambots.meta.api.objects.User from = update.getMessage().getFrom();
        User user = User.builder()
                .id(from.getId())
                .username(from.getUserName())
                .firstname(from.getFirstName())
                .lastname(from.getLastName())
                .build();
        user.addChat(update.getMessage().getChatId());
        try {
            if(userService.getAll().containsKey(user.getId())) {
                resolver.sendMessage(update, "Вы уже активировали бота");
            }else {
                userService.createUser(user);
                resolver.sendMessage(update, "Вы успешно авторизовались!");
            }
        }catch (IllegalArgumentException e){
            resolver.sendMessage(update, e.getMessage());
        }
    }
}
