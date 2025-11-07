package fr.utcapitole.demo.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Question extends Message {
    private String title;
    @ManyToOne
    private Category category;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    
}
