package chatbot.infrastructure.external;

import chatbot.application.service.AIChatService;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value; // Make sure this import is present
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "ai.service.mock", havingValue = "false", matchIfMissing = true)
public class GeminiAiChatService implements AIChatService {

    private final Client geminiClient;

    public GeminiAiChatService(@Value("${google.api.key}") String apiKey) {
        this.geminiClient = new Client();
    }

    @Override
    public String generateResponse(String prompt) {
        try {

            GenerateContentResponse response = geminiClient.models.generateContent("gemini-2.0-flash", prompt, null);
            return response.text();
        } catch (Exception e) {
            throw new RuntimeException("Error generating AI response: " + e.getMessage(), e);
        }
    }
}