package fr.utcapitole.demo.gui;

import fr.utcapitole.demo.repositories.CategoryRepository;
import fr.utcapitole.demo.repositories.QuestionRepository;
import fr.utcapitole.demo.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebAppController {
    private final CategoryRepository categoryRepository;
    private QuestionRepository questionRepository;
    private UserRepository userRepository;

    public WebAppController(CategoryRepository categoryRepository, QuestionRepository questionRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/")
    public String home(final ModelMap model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "home";
    }

}
