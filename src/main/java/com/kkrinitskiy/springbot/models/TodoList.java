package com.kkrinitskiy.springbot.models;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "todos")
public class TodoList {
    private User user;
    private List<Todo> todos;

    public void addTodo(Todo todo) {
        todos.add(todo);
        todo.setTodoList(this);
    }

    public void removeTodo(Todo todo) {
        todos.remove(todo);
    }

    public void toggleTodo(Todo todo) {
        todos.stream().filter(t -> t.equals(todo)).findFirst().ifPresent(t -> t.setCompleted(!t.getCompleted()));
    }

    public void setUser(User user) {
        this.user = user;
        user.setTodoList(this);
    }
    

}
