package com.kkrinitskiy.springbot.repositories;

import com.kkrinitskiy.springbot.models.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository

public class UserRepository {

    Map<Long, User> users = new HashMap<>();

    public Map<Long, User> findAll() {
        return users;
    }
    public User getUser(long id) {
        return users.get(id);
    }
    public void saveUser(User user) throws IllegalArgumentException {
        users.put(user.getId(), user);
    }
    public void removeUser(User user) {
        users.remove(user.getId());
    }

    public void removeAll() {
        users = new HashMap<>();
    }
}
