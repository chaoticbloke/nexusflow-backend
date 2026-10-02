package io.canduer.nexusflow.ai.repository;

import io.canduer.nexusflow.ai.entity.KnowledgeChunkEntity;
import io.canduer.nexusflow.ai.entity.KnowledgeSearchProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KnowledgeChunkRepository extends JpaRepository<KnowledgeChunkEntity, Long> {

    Optional<KnowledgeChunkEntity> findByChunkId(String chunkId);

    @Query(value = """
        SELECT
            chunk_id AS chunkId,
            content AS content,
            embedding <=> CAST(:embedding AS vector) AS distance
        FROM knowledge_chunks
        ORDER BY embedding <=> CAST(:embedding AS vector)
        LIMIT :limit
        """, nativeQuery = true)
    List<KnowledgeSearchProjection> findSimilar(@Param("embedding") String embedding, @Param("limit") int limit);
}