package fr.utcapitole.demo.repositories;

import fr.utcapitole.demo.entities.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    long countByAuthor(String author);
}