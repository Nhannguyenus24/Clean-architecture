package chatbot.Interface.controller;

import chatbot.Interface.dto.ResponseDto;
import chatbot.application.usecase.CreateConversationUseCase;
import chatbot.application.usecase.SendMessageUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final SendMessageUseCase sendMessageUseCase;
    private final CreateConversationUseCase createConversationUseCase;

    public ChatController(SendMessageUseCase sendMessageUseCase,
                          CreateConversationUseCase createConversationUseCase) {
        this.sendMessageUseCase = sendMessageUseCase;
        this.createConversationUseCase = createConversationUseCase;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto<Integer>> createConversation(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam String name) {

        String token = authorizationHeader.replace("Bearer ", "");
        var result = createConversationUseCase.execute(token, name);

        if (!result.isSuccess()) {
            return ResponseEntity.status(500)
                    .body(new ResponseDto<>(result.getMessage(), null, 500));
        }

        return ResponseEntity.ok(new ResponseDto<>("Created", result.getConversationId(), 200));
    }

    @PostMapping("/{conversationId}")
    public ResponseEntity<ResponseDto<String>> chat(
            @PathVariable Integer conversationId,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody String prompt) {

        String token = authorizationHeader.replace("Bearer ", "");
        SendMessageUseCase.SendMessageResult result = sendMessageUseCase.execute(conversationId, token, prompt);

        if (!result.isSuccess()) {
            int statusCode = result.getMessage().contains("not found") || result.getMessage().contains("access denied") ? 403 : 500;
            return ResponseEntity.status(statusCode)
                    .body(new ResponseDto<>(result.getMessage(), null, statusCode));
        }

        return ResponseEntity.ok(new ResponseDto<>("Success", result.getAiResponse(), 200));
    }
}
