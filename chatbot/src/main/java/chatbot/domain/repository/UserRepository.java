package chatbot.domain.repository;

import chatbot.domain.entity.User;
import java.util.Optional;
import java.util.List;

public interface UserRepository {
    Optional<User> findById(String id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    void save(User user);
    void deleteById(String id);
}
