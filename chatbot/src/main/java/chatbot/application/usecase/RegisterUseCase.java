package chatbot.application.usecase;

import chatbot.domain.entity.User;
import chatbot.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegisterUseCase {
    
    private final UserRepository userRepository;
    
    public RegisterUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public RegisterResult execute(String name, String email, String password) {
        // Check if user already exists
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return new RegisterResult(false, "Email already in use");
        }
        
        // Generate ID (simple approach - in real app should use proper ID generation)
        int userId = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
        
        // Create and save new user
        User newUser = new User(userId, name, email, password);
        userRepository.save(newUser);
        
        return new RegisterResult(true, "User registered successfully");
    }
    
    public static class RegisterResult {
        private final boolean success;
        private final String message;
        
        public RegisterResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
} 