package fr.utcapitole.demo.api;

import fr.utcapitole.demo.entities.Answer;
import fr.utcapitole.demo.entities.Message;
import fr.utcapitole.demo.entities.Question;
import fr.utcapitole.demo.entities.QuestionAnswer;
import fr.utcapitole.demo.repositories.AnswerRepository;
import fr.utcapitole.demo.repositories.QuestionAnswerRepository;
import fr.utcapitole.demo.repositories.QuestionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}, allowCredentials = "true")
public class AnswerController {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuestionAnswerRepository qaRepository;

    public AnswerController(QuestionRepository questionRepository, AnswerRepository answerRepository, QuestionAnswerRepository qaRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.qaRepository = qaRepository;
    }

    // Submit a new answer for a question
    @PostMapping(value = "/questions/{questionId}/answers", produces = "application/json")
    public ResponseEntity<Map<String, Object>> answerQuestion(@PathVariable int questionId, @RequestBody AnswerPayload payload) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found"));

        if (payload == null || payload.getContent() == null || payload.getContent().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Answer content is required");
        }

        Answer answer = new Answer();
        answer.setAuthor(payload.getAuthor());
        answer.setContent(payload.getContent());
        answer.setCreatedAt(OffsetDateTime.now());
        Answer savedAnswer = answerRepository.save(answer);

        QuestionAnswer qa = new QuestionAnswer();
        qa.setQuestionMessage(question); // question is a Message subclass
        qa.setAnswerMessage(savedAnswer); // answer is a Message subclass
        qaRepository.save(qa);

        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("answer", savedAnswer);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // List all answers for a question
    @GetMapping(value = "/questions/{questionId}/answers", produces = "application/json")
    public List<Answer> getAnswersForQuestion(@PathVariable int questionId) {
        // Ensure question exists
        questionRepository.findById(questionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found"));

        return qaRepository.findByQuestionMessage_Id(questionId)
                .stream()
                .map(qa -> (Answer) qa.getAnswerMessage())
                .collect(Collectors.toList());
    }

    // Given an answer, fetch its question
    @GetMapping(value = "/answers/{answerId}/question", produces = "application/json")
    public Message getQuestionForAnswer(@PathVariable int answerId) {
        QuestionAnswer qa = qaRepository.findByAnswerMessage_Id(answerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not linked to a question"));
        return qa.getQuestionMessage();
    }

    // Payload DTO for creating answers
    public static class AnswerPayload {
        private String author;
        private String content;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}