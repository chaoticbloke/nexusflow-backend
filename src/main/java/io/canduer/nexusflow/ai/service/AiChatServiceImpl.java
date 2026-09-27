package io.canduer.nexusflow.ai.service;

import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import io.canduer.nexusflow.ai.dto.ChatRequest;
import io.canduer.nexusflow.ai.dto.ChatResponse;
import io.canduer.nexusflow.ai.knowledge.KnowledgeChunk;
import io.canduer.nexusflow.ai.knowledge.KnowledgeRetriever;
import io.canduer.nexusflow.ai.knowledge.KnowledgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final OpenAIClient openAIClient;
    private final ConversationStore conversationStore;
    private final KnowledgeService knowledgeService;
    private final KnowledgeRetriever knowledgeRetriever;

    @Override
    public ChatResponse chat(ChatRequest request) {

        // Get previous messages for this conversation
        var history = conversationStore.getMessages(request.getConversationId());


        // Build conversation input
        String conversation = history.stream().map(message -> message.getRole() + ": " + message.getContent())
                .collect(Collectors.joining("\n"));

        // Add the current user message
        //not req for RAG
       // String input = conversation.isBlank() ? "USER: " + request.getMessage() : conversation + "\nUSER: " + request.getMessage();

        List<KnowledgeChunk> knowledgeChunks = knowledgeService.getKnowledgeChunks();
        List<KnowledgeChunk> relevantChunks = knowledgeRetriever.retrieve(request.getMessage());
        String knowledge = relevantChunks.stream().map(KnowledgeChunk::getContent).collect(Collectors.joining("\n\n"));


        String input = """
        NEXUSFLOW KNOWLEDGE:
        %s

        CONVERSATION:
        %s

        USER QUESTION:
        %s
        """.formatted(
                knowledge,
                conversation,
                request.getMessage()
        );

        ResponseCreateParams params = ResponseCreateParams.builder()
                .model(ChatModel.GPT_5_2)
                .input(input)
                .build();

        Response response = openAIClient.responses().create(params);

        String answer = response.output().stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .map(outputText -> outputText.text())
                .collect(Collectors.joining());

        // Store the conversation
        conversationStore.addMessage(request.getConversationId(), "USER", request.getMessage());

        conversationStore.addMessage(request.getConversationId(), "ASSISTANT", answer);

        return ChatResponse.builder()
                .message(answer)
                .build();
    }
}