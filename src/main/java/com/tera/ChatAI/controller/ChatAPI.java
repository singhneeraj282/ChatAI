package com.tera.ChatAI.controller;

import com.tera.ChatAI.dto.ChatDTO;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping(value = "/api/v1")
public interface ChatAPI {

    @GetMapping(value = "/chat")
    ResponseEntity<String> chat(@RequestParam String prompt);

    @PostMapping(value = "/chatWithDocument", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<String> chatWithDocument(@ModelAttribute @Valid ChatDTO dto, @RequestPart MultipartFile file);

    @PostMapping(value = "/chatWithSegments", consumes = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<String> chatWithSegments(@ModelAttribute @Valid ChatDTO dto);
}
