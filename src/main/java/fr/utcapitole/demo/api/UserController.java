package fr.utcapitole.demo.api;

import fr.utcapitole.demo.entities.User;
import fr.utcapitole.demo.repositories.AnswerRepository;
import fr.utcapitole.demo.repositories.QuestionRepository;
import fr.utcapitole.demo.repositories.MessageRepository;
import fr.utcapitole.demo.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/users")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.OPTIONS}, allowCredentials = "true")
public class UserController {

    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final MessageRepository messageRepository;

    public UserController(UserRepository userRepository, AnswerRepository answerRepository, QuestionRepository questionRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.messageRepository = messageRepository;
    }

    @GetMapping(value = "/{username}/stats", produces = "application/json")
    public ResponseEntity<Map<String, Object>> getUserStats(@PathVariable String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        long answers = answerRepository.countByAuthor(username);
        long questions = questionRepository.countByAuthor(username);

        // Compute day streak: consecutive days up to today with at least one post
        int streak = 0;
        try {
            var timestamps = messageRepository.findCreatedAtsByAuthor(username);
            var days = new java.util.HashSet<java.time.LocalDate>();
            for (var ts : timestamps) {
                if (ts != null) {
                    days.add(ts.toLocalDate());
                }
            }
            java.time.LocalDate cursor = java.time.LocalDate.now();
            while (days.contains(cursor)) {
                streak++;
                cursor = cursor.minusDays(1);
            }
        } catch (Exception ignored) {
            // If anything goes wrong, keep streak at 0 to avoid breaking the API
        }

        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", user.getUsername());
        userInfo.put("email", user.getEmail());
        res.put("user", userInfo);
        Map<String, Object> stats = new HashMap<>();
        stats.put("answers", answers);
        stats.put("questions", questions);
        stats.put("streak", streak);
        res.put("stats", stats);
        return ResponseEntity.ok(res);
    }
}