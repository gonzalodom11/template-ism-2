package fr.utcapitole.demo.api;

import fr.utcapitole.demo.entities.Category;
import fr.utcapitole.demo.entities.Question;
import fr.utcapitole.demo.repositories.CategoryRepository;
import fr.utcapitole.demo.repositories.QuestionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/category")
public class CategoryController {
    private final CategoryRepository repository;
    private final QuestionRepository questionRepository;

    public CategoryController(CategoryRepository repository, QuestionRepository questionRepository) {
        this.repository = repository;
        this.questionRepository = questionRepository;
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public void put(@PathVariable String id) {
        repository.save(new Category(id));
    }

    @GetMapping(value = "/{id}/question", produces = "application/json")
    public List<Question> get(@PathVariable String id) {
        return questionRepository.findByCategory(repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @DeleteMapping(value = "/", produces = "application/json")
    public void clear() {
        repository.deleteAll();
    }

}
