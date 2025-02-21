package me.trading_assistant.api.controller;


import me.trading_assistant.api.application.TrademateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);
    private final TrademateService trademateService;

    public UserController(TrademateService trademateService) {
        this.trademateService = trademateService;
    }


    @GetMapping("/api/users")
    public List<String> getUsers() {
        logger.info("Getting list of users");
        return trademateService.getUsers()
                .stream()
                .map(user -> user.getFirstname() + " " + user.getLastname())
                .toList();
    }

    @DeleteMapping("/api/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        logger.info("Deleting user with id {}", id);
        trademateService.deleteUser(id);
    }

    @GetMapping("/api/test")
    public String test() {
        return "Hello, World!";
    }

}