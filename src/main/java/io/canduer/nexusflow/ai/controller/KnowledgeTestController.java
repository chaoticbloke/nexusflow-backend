package io.canduer.nexusflow.ai.controller;

import io.canduer.nexusflow.ai.dto.ChatRequest;
import io.canduer.nexusflow.ai.dto.KnowledgeSearchResult;
import io.canduer.nexusflow.ai.service.KnowledgeRetrievalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai/knowledge")
@RequiredArgsConstructor
public class KnowledgeTestController {

    private final KnowledgeRetrievalService knowledgeRetrievalService;

    @PostMapping("/search")
    public List<KnowledgeSearchResult> search(@RequestBody ChatRequest chatRequest) {
        return knowledgeRetrievalService.retrieve(chatRequest.getMessage(), 3);
    }
}