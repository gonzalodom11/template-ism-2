package fr.utcapitole.demo.service;

import fr.utcapitole.demo.api.dto.QuestionDTO;
import fr.utcapitole.demo.entities.Question;
import fr.utcapitole.demo.repositories.QuestionAnswerRepository;
import fr.utcapitole.demo.repositories.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionAnswerRepository questionAnswerRepository;

    public QuestionService(QuestionRepository questionRepository, QuestionAnswerRepository questionAnswerRepository) {
        this.questionRepository = questionRepository;
        this.questionAnswerRepository = questionAnswerRepository;
    }

    public List<QuestionDTO> getAllQuestionsWithAnsweredStatus() {
        return questionRepository.findAll().stream()
                .map(question -> QuestionDTO.fromQuestion(
                    question, 
                    questionAnswerRepository.existsByQuestionMessage_Id(question.getId())
                ))
                .collect(Collectors.toList());
    }

    public Question getQuestionById(int id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
    }

    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    public void deleteAll() {
        questionRepository.deleteAll();
    }
}