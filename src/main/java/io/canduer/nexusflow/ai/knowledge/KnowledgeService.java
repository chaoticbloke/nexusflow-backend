package io.canduer.nexusflow.ai.knowledge;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Service
public class KnowledgeService {

    public List<KnowledgeChunk> getKnowledgeChunks() {

        try {
            ClassPathResource resource = new ClassPathResource("knowledge/nexusflow.md");

            String knowledge = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            return Arrays.stream(knowledge.split("(?=## )"))
                    .map(String::trim)
                    .filter(chunk -> !chunk.isBlank())
                    .map(chunk -> new KnowledgeChunk(
                            "chunk-" + Math.abs(chunk.hashCode()),
                            chunk
                    )).toList();

        } catch (IOException e) {
            throw new RuntimeException("Failed to load NexusFlow knowledge", e);
        }
    }
}