package io.canduer.nexusflow.ai.knowledge;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KnowledgeRetriever {

    private final KnowledgeService knowledgeService;

    public List<KnowledgeChunk> retrieve(String question) {

        List<KnowledgeChunk> chunks = knowledgeService.getKnowledgeChunks();

        return chunks.stream().filter(chunk -> containsKeyword(chunk.getContent(), question)).toList();
    }

    private boolean containsKeyword(String content, String question) {
        String lowerContent = content.toLowerCase();
        String lowerQuestion = question.toLowerCase();

        return lowerQuestion.split("\\s+").length > 0 && lowerQuestion.split("\\s+").length > 0
                && Arrays.stream(lowerQuestion.split("\\s+")).anyMatch(lowerContent::contains);
    }
}