package me.trading_assistant.api.application;

import me.trading_assistant.api.infrastructure.User;
import me.trading_assistant.api.infrastructure.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrademateService {

    private final UserRepository userRepository;

    public TrademateService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(userRepository::delete);
    }

    public void createUser(String firstname, String lastname, String email, Integer telephone, String password) {
        userRepository.save(new User(firstname, lastname, email, telephone, password));
    }



}
