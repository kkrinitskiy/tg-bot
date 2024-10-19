package com.kkrinitskiy.springbot.callbacks;

import com.kkrinitskiy.springbot.core.Resolver;
import com.kkrinitskiy.springbot.services.UserService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AdminAppBotCallback implements AppBotCallback {
    private UserService userService;
    private Resolver resolver;
    public AdminAppBotCallback(UserService userService, Resolver resolver) {
        this.userService = userService;
        this.resolver = resolver;
    }

    @Override
    public void handle(Update update, Resolver resolver) {
        if(update.getCallbackQuery().getData().contains("get_all_users")) {
            resolver.sendMessage(update, userService.getAll().toString());
            return;
        }
        if(update.getCallbackQuery().getData().contains("remove_all_users")) {

            userService.removeAll();
            resolver.sendMessage(update, "все пользователи были удалены");
            return;
        }
            resolver.sendMessage(update, "AppBotCallBack: неизвестная команда");
    }
}
