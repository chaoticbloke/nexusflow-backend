package io.canduer.nexusflow.ai.service;

import io.canduer.nexusflow.ai.dto.ChatRequest;
import io.canduer.nexusflow.ai.dto.ChatResponse;

public interface AiChatService {

    ChatResponse chat(ChatRequest request);
}