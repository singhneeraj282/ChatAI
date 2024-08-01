package com.tera.ChatAI.controller;

import com.tera.ChatAI.dto.ChatDTO;
import com.tera.ChatAI.entity.PersonalisedContent;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping(value = "/api/v1/chat")
public interface ChatAPI {

    @GetMapping(value = "/prompt")
    ResponseEntity<String> chat(@RequestParam String prompt);

    @GetMapping(value = "/azure/prompt")
    ResponseEntity<String> azureChat(@RequestParam String prompt);

    @PostMapping(value = "/promptWithDocument", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<String> chatWithDocument(@ModelAttribute @Valid ChatDTO dto, @RequestPart MultipartFile file);

    @PostMapping(value = "/promptWithSegments", consumes = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<String> chatWithSegments(@ModelAttribute @Valid ChatDTO dto);

    @GetMapping(value = "/azure/promptWithSegments")
    ResponseEntity<List<PersonalisedContent>> azureChatWithSegments(@ModelAttribute @Valid ChatDTO dto) throws IOException;
}
