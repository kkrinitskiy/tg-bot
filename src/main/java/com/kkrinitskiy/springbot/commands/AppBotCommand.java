package com.kkrinitskiy.springbot.commands;

import com.kkrinitskiy.springbot.core.Resolver;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface AppBotCommand {
    void handle(Update update, Resolver resolver);
}
