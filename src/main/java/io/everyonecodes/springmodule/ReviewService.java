package io.everyonecodes.springmodule;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository repository;

    public ReviewService(ReviewRepository repository) {
        this.repository = repository;
    }

    public List<Review> getAll() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Review::getPublishedDate).reversed())
                .collect(Collectors.toList());
    }

    public Optional<Review> getOne(@PathVariable long id) {
        return repository.findById(id);
    }

    public Review createNew(@RequestBody Review review) {
        return repository.save(review);
    }

    public Review change(@PathVariable long id, Review review) {
        Optional<Review> existingReview = repository.findById(id);
        if (existingReview.isPresent()) {
            Review reviewToUpdate = existingReview.get();
            reviewToUpdate.setContent(review.getContent());
            reviewToUpdate.setScore(review.getScore());
            return repository.save(reviewToUpdate);
        } else {
            return repository.save(review);
        }
    }

    public Optional<String> delete(@RequestBody long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
            return Optional.of(String.valueOf(id));
        }
        return Optional.empty();
    }
}
