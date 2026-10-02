package io.canduer.nexusflow.ai.service;
import io.canduer.nexusflow.ai.dto.KnowledgeSearchResult;
import io.canduer.nexusflow.ai.embedding.EmbeddingService;
import io.canduer.nexusflow.ai.entity.KnowledgeChunkEntity;
import io.canduer.nexusflow.ai.repository.KnowledgeChunkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KnowledgeRetrievalService {

    private final EmbeddingService embeddingService;

    private final KnowledgeChunkRepository knowledgeChunkRepository;

    public List<KnowledgeSearchResult> retrieve(String question, int limit) {

        List<Float> embedding = embeddingService.generateEmbedding(question);

        String vector = embedding.toString();

        return knowledgeChunkRepository.findSimilar(vector, limit)
                .stream()
                .map(result -> new KnowledgeSearchResult(
                        result.getChunkId(),
                        result.getContent(),
                        result.getDistance()
                ))
                .toList();
    }
}