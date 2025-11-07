package fr.utcapitole.demo.api;

import fr.utcapitole.demo.api.dto.QuestionDTO;
import fr.utcapitole.demo.entities.Question;
import fr.utcapitole.demo.entities.Category;
import fr.utcapitole.demo.repositories.QuestionRepository;
import fr.utcapitole.demo.service.QuestionService;
import fr.utcapitole.demo.repositories.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"api/questions", "api/question"})
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}, allowCredentials = "true")
public class QuestionController {
    private final QuestionRepository repository;
    private final CategoryRepository categoryRepository;
    private final QuestionService questionService;

    public QuestionController(QuestionRepository repository, CategoryRepository categoryRepository, QuestionService questionService) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.questionService = questionService;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public Question get(@PathVariable int id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping(produces = "application/json")
    public List<QuestionDTO> getAll() {
        return questionService.getAllQuestionsWithAnsweredStatus();
    }
    

    @PostMapping(produces = "application/json")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Question question) {
        try {
            // Handle category - if category is provided, fetch it from database
            if (question.getCategory() != null && question.getCategory().getId() != null) {
                Category existingCategory = categoryRepository.findById(question.getCategory().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
                question.setCategory(existingCategory);
            } else {
                // Use default category if none provided
                Category defaultCategory = categoryRepository.findById("general")
                    .orElseGet(() -> categoryRepository.save(new Category("general")));
                question.setCategory(defaultCategory);
        }
            
            // Set creation timestamp for the new question
            question.setCreatedAt(OffsetDateTime.now());

            Question savedQuestion = repository.save(question);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Question submitted successfully");
            response.put("question", savedQuestion);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to submit question: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping(value = "/", produces = "application/json")
    public void clear() {
        repository.deleteAll();
    }
}
