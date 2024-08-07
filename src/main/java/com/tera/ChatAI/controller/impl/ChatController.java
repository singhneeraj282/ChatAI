package com.tera.ChatAI.controller.impl;

import com.tera.ChatAI.controller.ChatAPI;
import com.tera.ChatAI.dto.ChatDTO;
import com.tera.ChatAI.entity.PersonalisedContent;
import com.tera.ChatAI.service.ChatSegmentService;
import com.tera.ChatAI.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatController implements ChatAPI {

    private final ChatSegmentService chatService;

    @Override
    public ResponseEntity<String> chat(String message) {
        return ResponseEntity.ok(chatService.chat(message));
    }

    @Override
    public ResponseEntity<String> azureChat(String prompt) {
        return ResponseEntity.ok(chatService.azureChat(prompt));
    }

    @Override
    public ResponseEntity<String> chatWithDocument(ChatDTO dto, MultipartFile file) {
        return ResponseEntity.ok(chatService.chatWithDocument(dto, file));
    }

    @Override
    public ResponseEntity<String> chatWithSegments(ChatDTO dto) {
        return ResponseEntity.ok(chatService.chatWithSegments(dto));
    }

    @Override
    public ResponseEntity<List<PersonalisedContent>> azureChatWithSegments(ChatDTO dto) throws IOException {
        return ResponseEntity.ok(chatService.azureChatWithSegments());
    }
}
