package com.kkrinitskiy.springbot.callbacks;

import com.kkrinitskiy.springbot.core.Resolver;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface AppBotCallback {
    void handle(Update update, Resolver resolver);
}
