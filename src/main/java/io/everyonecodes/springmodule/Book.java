package io.everyonecodes.springmodule;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class Book {
    private static long nextId = 1;
    @Id
    @GeneratedValue
    private long id;

    private String title;

    private List<String> authors;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    public Book(String title, List<String> authors, List<Review> reviews) {
        this.id = generateNextId();
        this.title = title;
        this.authors = authors;
        this.reviews = reviews;
    }
    private static synchronized Long generateNextId() {
        return nextId++;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
