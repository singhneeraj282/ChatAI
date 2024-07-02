package com.tera.ChatAI.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Setter;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

@Value
@Builder
@Setter
public class ChatDTO {
    @NotBlank
    String prompt;
}
