package chatbot.application.usecase;

import chatbot.application.service.ConversationService;
import chatbot.application.service.JwtEncodedService;
import org.springframework.stereotype.Service;

@Service
public class CreateConversationUseCase {

    private final ConversationService conversationService;
    private final JwtEncodedService jwtEncodedService;

    public CreateConversationUseCase(ConversationService conversationService, JwtEncodedService jwtEncodedService) {
        this.conversationService = conversationService;
        this.jwtEncodedService = jwtEncodedService;
    }

    public CreateConversationResult execute(String token, String name) {
        try {
            Integer userId = jwtEncodedService.decode(token);
            Integer conversationId = conversationService.createConversation(userId, name);
            return new CreateConversationResult(true, "Conversation created successfully", conversationId);
        } catch (Exception e) {
            return new CreateConversationResult(false, "Error: " + e.getMessage(), null);
        }
    }

    public static class CreateConversationResult {
        private final boolean success;
        private final String message;
        private final Integer conversationId;

        public CreateConversationResult(boolean success, String message, Integer conversationId) {
            this.success = success;
            this.message = message;
            this.conversationId = conversationId;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Integer getConversationId() { return conversationId; }
    }
}
