package io.canduer.nexusflow.ai.controller;

import io.canduer.nexusflow.ai.dto.ChatRequest;
import io.canduer.nexusflow.ai.dto.ChatResponse;
import io.canduer.nexusflow.ai.service.AiChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiChatController {

    private final AiChatService aiChatService;

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {

        return aiChatService.chat(request);
    }
}