package fr.utcapitole.demo.entities;

import javax.persistence.Entity;

@Entity
public class Answer extends Message {
    // No extra fields for now; inherits author, content, createdAt
}