package com.kkrinitskiy.springbot.models;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    @Builder.Default
    private List<Long> chatIds = new ArrayList<>();
    @Builder.Default
    private TodoList todoList = null;
    public void addChat(Long chatId) {
        chatIds.add(chatId);
    }
}
