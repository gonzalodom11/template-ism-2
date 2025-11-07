package fr.utcapitole.demo.api.dto;

import fr.utcapitole.demo.entities.Category;
import fr.utcapitole.demo.entities.Question;

public class QuestionDTO {
    private Integer id;
    private String content;
    private Category category;
    private boolean answered;
    private String title;
    private String author;

  

    public static QuestionDTO fromQuestion(Question question, boolean answered) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(question.getId());
        dto.setContent(question.getContent());
        dto.setCategory(question.getCategory());
        dto.setAnswered(answered);
        dto.setTitle(question.getTitle());
        dto.setAuthor(question.getAuthor());
        return dto;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String text) {
        this.content = text;
    }

    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isAnswered() {
        return answered;
    }
    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

      public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
}