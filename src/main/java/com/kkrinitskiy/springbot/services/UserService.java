package com.kkrinitskiy.springbot.services;

import com.kkrinitskiy.springbot.models.User;
import com.kkrinitskiy.springbot.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(User user) throws IllegalArgumentException {
        userRepository.saveUser(user);
    }

    public boolean findById(Long id) {
        return userRepository.getUser(id) != null;
    }

    public Map<Long, User> getAll() {
        return userRepository.findAll();
    }

    public void removeAll() {
        userRepository.removeAll();
    }
}
