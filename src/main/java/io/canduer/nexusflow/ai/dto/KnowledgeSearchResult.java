package io.canduer.nexusflow.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KnowledgeSearchResult {

    private String chunkId;
    private String content;
    private double distance;
}
