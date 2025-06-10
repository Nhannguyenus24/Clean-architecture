package chatbot.application.usecase;

import chatbot.application.service.ConversationService;
import chatbot.application.service.JwtEncodedService;
import chatbot.domain.entity.Conversation;
import chatbot.domain.entity.Message;
import chatbot.domain.port.AiChatService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SendMessageUseCase {
    
    private final ConversationService conversationService;
    private final JwtEncodedService jwtEncodedService;
    private final AiChatService aiChatService;
    
    public SendMessageUseCase(ConversationService conversationService, 
                             JwtEncodedService jwtEncodedService,
                             AiChatService aiChatService) {
        this.conversationService = conversationService;
        this.jwtEncodedService = jwtEncodedService;
        this.aiChatService = aiChatService;
    }
    
    public SendMessageResult execute(Integer conversationId, String token, String prompt) {
        try {
            Integer userId = jwtEncodedService.decode(token);
            
            // Validate user owns conversation
            Optional<Conversation> conversationOpt = conversationService
                    .getUserConversations(userId)
                    .stream()
                    .filter(conv -> conv.getId().equals(conversationId))
                    .findFirst();
            
            if (conversationOpt.isEmpty()) {
                return new SendMessageResult(false, "Conversation not found or access denied", null);
            }
            
            // Generate AI response
            String reply = aiChatService.generateResponse(prompt);
            
            // Save user message
            conversationService.addMessage(conversationId,
                    new Message(null, prompt, LocalDateTime.now(), true));
            
            // Save AI response
            conversationService.addMessage(conversationId,
                    new Message(null, reply, LocalDateTime.now(), false));
            
            return new SendMessageResult(true, "Message sent successfully", reply);
            
        } catch (Exception e) {
            return new SendMessageResult(false, "Error processing message: " + e.getMessage(), null);
        }
    }
    
    public static class SendMessageResult {
        private final boolean success;
        private final String message;
        private final String aiResponse;
        
        public SendMessageResult(boolean success, String message, String aiResponse) {
            this.success = success;
            this.message = message;
            this.aiResponse = aiResponse;
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getAiResponse() { return aiResponse; }
    }
} 