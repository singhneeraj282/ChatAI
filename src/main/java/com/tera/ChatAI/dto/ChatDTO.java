package com.tera.ChatAI.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
public class ChatDTO implements Serializable {
    @NotBlank
    String message;
}
