package ch.fhnw.bookstore.catalog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Book {

    @Id
    private String isbn;

    private String title;

    @Column(length = 2000)
    private String description;

    private String author;

    protected Book() {
        // für JPA
    }

    public Book(String isbn, String title, String description, String author) {
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.author = author;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getAuthor() { return author; }

    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setAuthor(String author) { this.author = author; }
}
