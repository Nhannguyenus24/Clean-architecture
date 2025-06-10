package chatbot.application.service;

import chatbot.domain.entity.Conversation;
import chatbot.domain.entity.Message;
import chatbot.domain.repository.ConversationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConversationService {
    private final ConversationRepository conversationRepository;

    public ConversationService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public Integer createConversation(Integer userId, String name) {
        Conversation conversation = new Conversation(null, name, userId, LocalDateTime.now());
        conversationRepository.save(conversation);
        return conversation.getId();
    }

    public List<Conversation> getUserConversations(Integer userId) {
        return conversationRepository.findByUserId(userId);
    }

    public List<Message> getMessages(Integer conversationId) {
        return conversationRepository.getMessages(conversationId);
    }

    public void addMessage(Integer conversationId, Message message) {
        conversationRepository.addMessage(conversationId, message);
    }
}
