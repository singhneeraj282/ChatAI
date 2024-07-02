package com.tera.ChatAI.controller.impl;

import com.tera.ChatAI.controller.ChatAPI;
import com.tera.ChatAI.dto.ChatDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class ChatController implements ChatAPI {

    private final OpenAiChatModel chatModel;

    @Override
    public ResponseEntity<String> chat(String message) {
        return ResponseEntity.ok(chatModel.call(message));
    }

    @Override
    public ResponseEntity<String> chatWithDocument(ChatDTO dto, MultipartFile file) {
        return ResponseEntity.ok(chatModel.call(new UserMessage(dto.getMessage()), new SystemMessage(file.getResource())));
    }
}
