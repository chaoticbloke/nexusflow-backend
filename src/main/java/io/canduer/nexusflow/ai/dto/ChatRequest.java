package io.canduer.nexusflow.ai.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequest {

    private String conversationId;
    private String message;
}