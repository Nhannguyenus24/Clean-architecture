package chatbot.infrastructure.external;

import chatbot.domain.port.AiChatService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "ai.service.mock", havingValue = "true")
public class MockAiChatService implements AiChatService {

    @Override
    public String generateResponse(String prompt) {
        // Return a mock response for testing
        return "Mock AI Response: I received your message '" + prompt + "'. This is a simulated response for testing purposes.";
    }
} 