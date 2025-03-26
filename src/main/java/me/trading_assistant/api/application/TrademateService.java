package me.trading_assistant.api.application;

import me.trading_assistant.api.infrastructure.User;
import me.trading_assistant.api.infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TrademateService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long user_id) {
        return userRepository.findById(user_id).orElse(null);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long user_id) {
        userRepository.deleteById(user_id);
    }


}
