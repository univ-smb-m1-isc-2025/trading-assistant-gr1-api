package me.trading_assistant.api.controller;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class controller {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/info")
    public Map<String, String> info() {
        return Map.of(
            "name", "Trading Assistant API",
            "version", "1.0.0"
        );
    }

}
