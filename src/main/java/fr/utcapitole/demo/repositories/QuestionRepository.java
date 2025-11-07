package fr.utcapitole.demo.repositories;

import fr.utcapitole.demo.entities.Category;
import fr.utcapitole.demo.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByCategory(Category category);

    List<Question> findByAuthor(String author);

    long countByAuthor(String author);
}
