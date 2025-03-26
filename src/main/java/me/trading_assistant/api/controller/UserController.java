package me.trading_assistant.api.controller;

import me.trading_assistant.api.infrastructure.User;
import me.trading_assistant.api.application.TrademateService;
import me.trading_assistant.api.infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private TrademateService trademateService;

    @GetMapping
    public List<User> getAllUsers() {
        return trademateService.getAllUsers();
    }

    @GetMapping("/{user_id}")
    public User getUserById(@PathVariable Long user_id) {
        return trademateService.getUserById(user_id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return trademateService.saveUser(user);
    }

    @DeleteMapping("/{user_id}")
    public void deleteUser(@PathVariable Long user_id) {
        trademateService.deleteUser(user_id);
    }

}