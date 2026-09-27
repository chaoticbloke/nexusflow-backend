package io.canduer.nexusflow.ai.service;

import io.canduer.nexusflow.ai.dto.ConversationMessage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConversationStore {

    private final ConcurrentHashMap<String, List<ConversationMessage>> conversations = new ConcurrentHashMap<>();

    public List<ConversationMessage> getMessages(String conversationId) {
        return conversations.getOrDefault(conversationId, new ArrayList<>());
    }

    public void addMessage(String conversationId, String role, String content) {
        conversations.computeIfAbsent(conversationId, key -> new ArrayList<>()).add(new ConversationMessage(role, content));
    }
}