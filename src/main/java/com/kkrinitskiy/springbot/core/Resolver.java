package com.kkrinitskiy.springbot.core;

import com.kkrinitskiy.springbot.callbacks.AdminAppBotCallback;
import com.kkrinitskiy.springbot.callbacks.AppBotCallback;
import com.kkrinitskiy.springbot.commands.AdminAppBotCommand;
import com.kkrinitskiy.springbot.commands.AppBotCommand;
import com.kkrinitskiy.springbot.commands.StartAppBotCommand;
import com.kkrinitskiy.springbot.commands.TodoAppBotCommand;
import com.kkrinitskiy.springbot.services.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeAllGroupChats;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Resolver {
    private final Map<String, AppBotCommand> commandHandlers = new ConcurrentHashMap<>();
    private final Map<String, AppBotCallback> callbackHandlers = new ConcurrentHashMap<>();
    private final Map<String, String> adminMenu = new ConcurrentHashMap<>();
    private TelegramClient client;
    private UserService userService;

    public Resolver(TelegramClient client, UserService userService) {
        this.client = client;
        this.userService = userService;
        registerCommands();
        setAppBotCommands();
        setAppBotCallbacks();
        setAdminMenu();
    }

    private void setAppBotCommands() {
        commandHandlers.put("/todo", new TodoAppBotCommand(userService));
        commandHandlers.put("/start", new StartAppBotCommand(userService));
        commandHandlers.put("/admin", new AdminAppBotCommand(adminMenu));
    }

    private void setAppBotCallbacks() {
        callbackHandlers.put("/admin", new AdminAppBotCallback(userService, this));
    }

    private void setAdminMenu(){
        adminMenu.put("показать всех пользователей", "/admin_get_all_users");
        adminMenu.put("удалить всех пользователей", "/admin_remove_all_users");

    }


    public void resolveUpdate(Update update) {
        Long userId = getUserId(update);
        String message = getMessageText(update);
        String callbackData = getCallbackQueryButtonKey(update);

        if (update.getMessage() != null && message.startsWith("/")) {
            handleCommand(update);
        } else if (callbackData != null && !callbackData.isEmpty()) {
            handleCallbackQuery(update);
        }
//        } else if (message != null && !message.isEmpty()) {
//            handleDialogMode(userId, message);
//        }
    }

    private void handleCallbackQuery(Update update) {
        AppBotCallback handler;
        if(!update.getCallbackQuery().getData().startsWith("/admin")) {
            handler = callbackHandlers.get(update.getCallbackQuery().getData());
        }else {
            handler = callbackHandlers.get("/admin");
        }
        if (handler != null) {
            handler.handle(update, this);
        } else {
            sendMessage(update, String.format("неизвестная команда '%s'", update.getCallbackQuery().getData()));
        }
    }

    private void handleCommand(Update update) {
        AppBotCommand handler = commandHandlers.get(update.getMessage().getText());
        if (handler != null) {
            handler.handle(update, this);
        } else {
            sendMessage(update, String.format("Неизвестная команда '%s'", update.getMessage().getText()));
        }
    }

    public String getMessageText(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return update.getMessage().getText();
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData() != null) {
            return update.getCallbackQuery().getData();
        }
        return "";
    }

    private void registerCommands() {
        List<BotCommand> commands = new ArrayList<>();
        commands.add(BotCommand.builder().command("/start").description("Начать работу с ботом").build());
        commands.add(BotCommand.builder().command("/todo").description("Список дел").build());

        try {
            client.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
            client.execute(new SetMyCommands(commands, new BotCommandScopeAllGroupChats(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public Long getUserId(Update update) {
        if (update.hasMessage() && update.getMessage().getFrom() != null) {
            return update.getMessage().getFrom().getId();
        }

        if (update.hasCallbackQuery() && update.getCallbackQuery().getFrom() != null) {
            return update.getCallbackQuery().getFrom().getId();
        }

        return null;
    }

    public String getCallbackQueryButtonKey(Update update) {
        return update.hasCallbackQuery() ? update.getCallbackQuery().getData() : "";
    }

    public void sendMessage(Update update, String message) {
        if(!update.hasCallbackQuery()) {
            try {
                executeTelegramApiMethod(SendMessage.builder()
                        .chatId(update.getMessage().getChatId())
                        .text(String.format("@%s, %s",
                                update.getMessage().getFrom().getUserName(),
                                message))
                        .build());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            try {
                executeTelegramApiMethod(SendMessage.builder()
                        .chatId(update.getCallbackQuery().getMessage().getChatId())
                        .text(String.format("@%s, %s",
                                update.getCallbackQuery().getFrom().getUserName(),
                                message))
                        .build());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMenuButtons(Update update, String title, Map<String, String> buttons) {
        try {
            List<InlineKeyboardButton> list = buttons.entrySet().stream().map(entry -> (InlineKeyboardButton) InlineKeyboardButton.builder()
                    .text(entry.getKey())
                    .callbackData(entry.getValue())
                    .build()).toList();

            executeTelegramApiMethod(SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text(title)
                    .replyMarkup( InlineKeyboardMarkup
                            .builder()
                            .keyboardRow(
                                    new InlineKeyboardRow(list)
                            )
                            .build())
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private <T extends Serializable, Method extends BotApiMethod<T>> void executeTelegramApiMethod(Method method) throws TelegramApiException {
        client.execute(method);
    }
}
