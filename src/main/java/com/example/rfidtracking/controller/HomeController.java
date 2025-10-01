package com.example.rfidtracking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "RFID Tracking Application");
        response.put("version", "1.0.0");
        response.put("status", "running");
        response.put("endpoints", Map.of(
            "filiais", "/api/filiais",
            "motos", "/api/motos",
            "registros", "/api/registros",
            "health", "/actuator/health"
        ));
        return response;
    }
}
