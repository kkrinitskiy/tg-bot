package com.kkrinitskiy.springbot.repositories;

import com.kkrinitskiy.springbot.models.TodoList;
import com.kkrinitskiy.springbot.models.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class TodoListRepository {
    private UserRepository userRepository;
    private Map<Long, TodoList> todoLists = new HashMap();


    public void addTodoToListByUserId(Long userId) {
        if (todoLists.get(userId) != null) {
            throw new RuntimeException("У вас уже есть список дел");
        }
        TodoList todoList = new TodoList();
        User user = userRepository.getUser(userId);
        todoList.setUser(user);
    }

    public void removeTodoFromListByUserId(Long userId) {
        if (todoLists.get(userId) == null) {
            throw new RuntimeException("У вас отсутствует список дел");
        }
        TodoList todoList = todoLists.get(userId);
        todoList.setUser(null);
    }


    public TodoList getByUserId(long id) { return todoLists.get(id); }

}
