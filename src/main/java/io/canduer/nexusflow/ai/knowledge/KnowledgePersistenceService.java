package io.canduer.nexusflow.ai.knowledge;

import io.canduer.nexusflow.ai.embedding.EmbeddingService;
import io.canduer.nexusflow.ai.repository.KnowledgeChunkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.canduer.nexusflow.ai.entity.KnowledgeChunkEntity;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KnowledgePersistenceService {

    private final KnowledgeService knowledgeService;
    private final EmbeddingService embeddingService;
    private final KnowledgeChunkRepository knowledgeChunkRepository;

    @Transactional
    public void indexKnowledge() {

        List<KnowledgeChunk> chunks = knowledgeService.getKnowledgeChunks();

        for (KnowledgeChunk chunk : chunks) {

            List<Float> embedding = embeddingService.generateEmbedding(chunk.getContent());

            float[] embeddingArray = new float[embedding.size()];

            for (int i = 0; i < embedding.size(); i++) {
                embeddingArray[i] = embedding.get(i);
            }

            KnowledgeChunkEntity entity = new KnowledgeChunkEntity();

            entity.setChunkId(chunk.getId());
            entity.setContent(chunk.getContent());
            entity.setEmbedding(embeddingArray);

            knowledgeChunkRepository.save(entity);
        }
    }
}