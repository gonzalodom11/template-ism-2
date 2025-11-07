package fr.utcapitole.demo.repositories;

import fr.utcapitole.demo.entities.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Long> {

    // Returns all answers linked to a given question message id
    List<QuestionAnswer> findByQuestionMessage_Id(int questionMessageId);
    // Returns the unique question linked to a given answer message id
    Optional<QuestionAnswer> findByAnswerMessage_Id(int answerMessageId);
    boolean existsByQuestionMessage_Id(int questionId);

}