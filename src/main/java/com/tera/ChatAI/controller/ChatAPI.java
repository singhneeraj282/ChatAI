package com.tera.ChatAI.controller;

import com.tera.ChatAI.dto.ChatDTO;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping(value = "/api/v1")
public interface ChatAPI {

    @PostMapping(value = "/chat", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<String> chat(@ModelAttribute @Valid ChatDTO dto, @RequestPart MultipartFile file);
}
