package io.canduer.nexusflow.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConversationMessage {

    private String role;
    private String content;
}