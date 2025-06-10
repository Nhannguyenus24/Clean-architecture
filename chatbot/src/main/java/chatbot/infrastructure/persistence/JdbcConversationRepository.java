package chatbot.infrastructure.persistence;

import chatbot.domain.entity.Conversation;
import chatbot.domain.entity.Message;
import chatbot.domain.repository.ConversationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcConversationRepository implements ConversationRepository {

    private static final Logger logger = LoggerFactory.getLogger(JdbcConversationRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public JdbcConversationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Conversation> conversationRowMapper = (rs, rowNum) -> new Conversation(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getInt("user_id"),
            rs.getTimestamp("created_at").toLocalDateTime()
    );

    private final RowMapper<Message> messageRowMapper = (rs, rowNum) -> new Message(
            rs.getInt("id"),
            rs.getString("content"),
            rs.getTimestamp("timestamp").toLocalDateTime(),
            rs.getBoolean("is_user")
    );

    @Override
    public Optional<Conversation> findById(Integer id) {
        String sql = "SELECT * FROM conversations WHERE id = ?";
        try {
            List<Conversation> result = jdbcTemplate.query(sql, conversationRowMapper, id);
            return result.stream().findFirst();
        } catch (DataAccessException e) {
            logger.error("Error finding conversation by ID {}: {}", id, e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Conversation> findByUserId(Integer userId) {
        String sql = "SELECT * FROM conversations WHERE user_id = ?";
        try {
            return jdbcTemplate.query(sql, conversationRowMapper, userId);
        } catch (DataAccessException e) {
            logger.error("Error finding conversations for user ID {}: {}", userId, e.getMessage());
            return List.of(); // Return empty list as fallback
        }
    }

    @Override
    public void save(Conversation conversation) {
        String sql = "INSERT INTO conversations (id, name, user_id, created_at) VALUES (?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, conversation.getId(), conversation.getName(), conversation.getUserId(),
                    Timestamp.valueOf(conversation.getDateTime()));
        } catch (DataAccessException e) {
            logger.error("Error saving conversation {}: {}", conversation, e.getMessage());
        }
    }

    @Override
    public List<Message> getMessages(Integer conversationId) {
        String sql = "SELECT * FROM messages WHERE conversation_id = ? ORDER BY timestamp ASC";
        try {
            return jdbcTemplate.query(sql, messageRowMapper, conversationId);
        } catch (DataAccessException e) {
            logger.error("Error getting messages for conversation ID {}: {}", conversationId, e.getMessage());
            return List.of();
        }
    }

    @Override
    public void addMessage(Integer conversationId, Message message) {
        String sql = "INSERT INTO messages (id, content, timestamp, is_user, conversation_id) VALUES (?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, message.getId(), message.getContent(),
                    Timestamp.valueOf(message.getTimestamp()), message.getIsUser(), conversationId);
        } catch (DataAccessException e) {
            logger.error("Error adding message to conversation ID {}: {}", conversationId, e.getMessage());
        }
    }
}