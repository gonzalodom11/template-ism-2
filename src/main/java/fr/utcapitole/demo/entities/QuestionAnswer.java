package fr.utcapitole.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Column;
import javax.persistence.ForeignKey;
import java.time.OffsetDateTime;

@Entity
@Table(
        name = "question_answer",
        uniqueConstraints = {
                @UniqueConstraint(name = "uniq_answer_message", columnNames = {"answer_message_id"})
        },
        indexes = {
                @Index(name = "idx_question_message_id", columnList = "question_message_id"),
                @Index(name = "idx_answer_message_id", columnList = "answer_message_id")
        }
)
public class QuestionAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    // Many answers can reference the same question
    @ManyToOne(optional = false, targetEntity = Question.class)
    @JoinColumn(name = "question_message_id", referencedColumnName = "id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_qa_question_message"))
    private Message questionMessage;

    // Each answer message can be associated to exactly one question
    @OneToOne(optional = false, targetEntity = Message.class)
    @JoinColumn(name = "answer_message_id", referencedColumnName = "id", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_qa_answer_message"))
    private Message answerMessage;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
        validate();
    }

    @PreUpdate
    public void preUpdate() {
        validate();
    }

    private void validate() {
        if (questionMessage == null || answerMessage == null) {
            throw new IllegalStateException("Both questionMessage and answerMessage must be set");
        }
        if (questionMessage.getId() == answerMessage.getId()) {
            throw new IllegalStateException("Question and answer cannot reference the same message");
        }
        if (!(questionMessage instanceof Question)) {
            throw new IllegalStateException("questionMessage must be a Question");
        }
    }

    public Long getId() {
        return id;
    }

    public Message getQuestionMessage() {
        return questionMessage;
    }

    public void setQuestionMessage(Message questionMessage) {
        this.questionMessage = questionMessage;
    }

    public Message getAnswerMessage() {
        return answerMessage;
    }

    public void setAnswerMessage(Message answerMessage) {
        this.answerMessage = answerMessage;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}