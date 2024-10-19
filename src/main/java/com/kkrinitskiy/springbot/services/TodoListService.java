package com.kkrinitskiy.springbot.services;

import com.kkrinitskiy.springbot.models.TodoList;
import com.kkrinitskiy.springbot.models.User;
import com.kkrinitskiy.springbot.repositories.TodoListRepository;
import com.kkrinitskiy.springbot.repositories.TodoRepository;
import org.springframework.stereotype.Service;

@Service
public class TodoListService {
    private TodoListRepository todoListRepository;
    private TodoRepository todoRepository;

    public TodoListService(TodoListRepository todoListRepository, TodoRepository todoRepository) {
        this.todoListRepository = todoListRepository;
        this.todoRepository = todoRepository;
    }

    public TodoList getTodoList(Long id) {
        return todoListRepository.getByUserId(id);
    }

    public void createTodoList(User user) {
        user.setTodoList(new TodoList());
    }
}
