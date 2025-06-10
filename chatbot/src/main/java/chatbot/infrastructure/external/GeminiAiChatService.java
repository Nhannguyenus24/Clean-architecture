//package chatbot.infrastructure.external;
//
//import chatbot.application.service.AIChatService;
//import com.google.genai.Client;
//import com.google.genai.types.GenerateContentResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//
//@Service
//@ConditionalOnProperty(name = "ai.service.mock", havingValue = "false", matchIfMissing = true)
//public class GeminiAiChatService implements AIChatService {
//
//    private static final Logger logger = LoggerFactory.getLogger(GeminiAiChatService.class);
//
//    private final Client geminiClient;
//    private final StringRedisTemplate redisTemplate;
//
//    public GeminiAiChatService(
//            @Value("${google.api.key}") String apiKey,
//            StringRedisTemplate redisTemplate
//    ) {
//        this.geminiClient = new Client();
//        this.redisTemplate = redisTemplate;
//    }
//
//    @Override
//    public String generateResponse(String prompt, Integer conversationId) {
//        String summaryKey = "summary:" + conversationId;
//        try {
//            logger.info("Generating AI response for conversationId={} with prompt='{}'", conversationId, prompt);
//
//            // Step 1: Get current summary from Redis
//            String context = redisTemplate.opsForValue().get(summaryKey);
//            if (context == null) context = "";
//            logger.debug("Retrieved summary context for {}: {}", summaryKey, context);
//
//            // Step 2: Build full prompt
//            String fullPrompt = "\n\nPrevious context summary (may be outdated or irrelevant): " + context + "\n\nUser: " + prompt;
//
//            // Step 3: Call Gemini
//            GenerateContentResponse response = geminiClient.models.generateContent("gemini-2.0-flash", fullPrompt, null);
//            String reply = response.text();
//            logger.info("Received AI reply for conversationId={}: {}", conversationId, reply);
//
//            // Step 4: Create a new summary
//            String summaryPrompt = "Summarize the conversation so far of user:\n\n"
//                    + "User: " + prompt + "\nPrevious context: " + context;
//
//            GenerateContentResponse summaryResponse = geminiClient.models.generateContent("gemini-2.0-flash", summaryPrompt, null);
//            String newSummary = summaryResponse.text();
//            logger.debug("Generated new summary for conversationId={}: {}", conversationId, newSummary);
//
//            // Step 5: Save new summary back to Redis
//            redisTemplate.opsForValue().set(summaryKey, newSummary, Duration.ofDays(30));
//            logger.info("Updated summary in Redis for key={}", summaryKey);
//
//            return reply;
//        } catch (Exception e) {
//            logger.error("Error generating AI response for conversationId={}: {}", conversationId, e.getMessage(), e);
//            throw new RuntimeException("Error generating AI response: " + e.getMessage(), e);
//        }
//    }
//}
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
    public String generateResponse(String prompt, Integer conversationId) {
        try {
            GenerateContentResponse response = geminiClient.models.generateContent("gemini-2.0-flash", prompt, null);
            return response.text();
        } catch (Exception e) {
            throw new RuntimeException("Error generating AI response: " + e.getMessage(), e);
        }
    }
}