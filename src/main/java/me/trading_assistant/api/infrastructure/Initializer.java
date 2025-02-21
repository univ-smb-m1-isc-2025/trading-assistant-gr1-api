package me.trading_assistant.api.infrastructure;

import org.springframework.stereotype.Service;

@Service
public class Initializer {

    private final UserRepository userRepository;

    public Initializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void init() {

        if (userRepository.findAll().isEmpty()) {
            userRepository.saveAndFlush(new User("John", "Doe", "johndoe@gmail.com", 123456789, "password"));
            userRepository.saveAndFlush(new User("Jane", "Doe", "janedoe@gmail.com", 987654321, "password"));
            userRepository.saveAndFlush(new User("Alice", "Doe", "alicedoe@gmail.com", 123456789, "password"));
        }

    }
}
