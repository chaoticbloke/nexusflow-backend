package io.canduer.nexusflow.ai.embedding;

import com.openai.client.OpenAIClient;
import com.openai.models.embeddings.CreateEmbeddingResponse;
import com.openai.models.embeddings.EmbeddingCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmbeddingService {

    private final OpenAIClient openAIClient;

    public List<Float> generateEmbedding(String text) {

        CreateEmbeddingResponse response = openAIClient.embeddings().create(EmbeddingCreateParams.builder()
                                .model("text-embedding-3-small")
                                .input(text)
                                .build());

        return response.data()
                .get(0)
                .embedding();
    }
}