package com.tera.ChatAI.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/v1")
public interface ChatAPI {

    @GetMapping("/chat")
    ResponseEntity<String> chat(@RequestParam String prompt);
}
