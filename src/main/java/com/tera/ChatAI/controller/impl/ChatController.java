package com.tera.ChatAI.controller.impl;

import com.tera.ChatAI.controller.ChatAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatController implements ChatAPI {

    private final OpenAiChatModel chatModel;

    @Override
    public ResponseEntity<String> chat(String prompt) {
        return ResponseEntity.ok(chatModel.call(prompt));
    }
}
