package com.tera.ChatAI.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tera.ChatAI.dto.ChatDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {

    private static final String systemText = """
            You are a helpful AI assistant that helps people find information.
            Your name is {name}
            You should reply to the user's request with your name and also in the style of a {voice}
            """;
    private final OpenAiChatModel chatModel;
    private final ObjectMapper objectMapper;

    public String chat(String message) {
        return chatModel.call(message);
    }

    public String chatWithDocument(ChatDTO dto, MultipartFile file) {
        return chatModel.call(new UserMessage(dto.getMessage()), new SystemMessage(file.getResource()));
    }

    public String chatWithSegments(ChatDTO dto) {
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        //all the  Segment will come from DB
        //for now giving the static genz segment, and this dto should be replaced with segmentBO which will come from BO
        String segment = "genz";
        return chatModel.call(new UserMessage(dto.getMessage(), new Media(MimeTypeUtils.APPLICATION_JSON, objectMapper.valueToTree(dto))),
                systemPromptTemplate.createMessage(Map.of("name", "Bank of Baroda", "voice", "Bank reception")));
    }
}
