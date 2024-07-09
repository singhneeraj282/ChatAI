package com.tera.ChatAI.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tera.ChatAI.controller.ChatAPI;
import com.tera.ChatAI.dto.ChatDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class ChatController implements ChatAPI {

    private final OpenAiChatModel chatModel;
    private final ObjectMapper objectMapper;

    String systemText = """
            You are a helpful AI assistant that helps people find information.
            Your name is {name}
            You should reply to the user's request with your name and also in the style of a {voice} from given {segment}
            """;

    @Override
    public ResponseEntity<String> chat(String message) {
        return ResponseEntity.ok(chatModel.call(message));
    }

    @Override
    public ResponseEntity<String> chatWithDocument(ChatDTO dto, MultipartFile file) {
        return ResponseEntity.ok(chatModel.call(new UserMessage(dto.getMessage()), new SystemMessage(file.getResource())));
    }

    @Override
    public ResponseEntity<String> chatWithSegments(ChatDTO dto) {
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        //all the  Segment will come from DB
        //for now giving the static genz segment, and this dto should be replaced with segmentBO which will come from BO
        String segment = "genz";
        return ResponseEntity.ok(chatModel.call(new UserMessage(dto.getMessage(), new Media(MimeTypeUtils.APPLICATION_JSON, objectMapper.valueToTree(dto))),
                systemPromptTemplate.createMessage(Map.of("name", "Bank of Baroda", "voice", "Bank reception", "segment", segment))));
    }
}
