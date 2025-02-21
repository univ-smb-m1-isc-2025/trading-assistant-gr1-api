package me.trading_assistant.api.controller;

import org.springframework.web.bind.annotation.*;


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


