package com.kkrinitskiy.springbot.models;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
class Todo {
    private TodoList todoList;
    private String text;
    private Boolean completed;
}