package io.everyonecodes.springmodule;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue
    private long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private int score;

    private LocalDateTime publishedDate;

    @ManyToOne()
    @JoinColumn(name = "book_id")
    private Book book;

    public Review(long id, String content, int score, LocalDateTime publishedDate, Book book) {
        this.id = id;
        this.content = content;
        this.score = score;
        this.publishedDate = publishedDate;
        this.book = book;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
