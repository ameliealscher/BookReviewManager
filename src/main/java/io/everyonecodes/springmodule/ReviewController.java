package io.everyonecodes.springmodule;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    @GetMapping("/review")
    List<Review> getAll() {
        return service.getAll();
    }

    @PostMapping("/review")
    Review createNew(@RequestBody Review review) {
        review.setPublishedDate(LocalDateTime.now());
        return service.createNew(review);
    }

    @GetMapping("/review/{id}")
    Review getOne(@PathVariable long id) {
        return service.getOne(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find review"));
    }

    @PutMapping("/change/{id}")
    Review change(@PathVariable long id, @RequestBody Review review) {
        return service.change(id, review);
    }

    @DeleteMapping("/delete/{id}")
    String delete(@PathVariable long id) {
        service.delete(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find review"));
        return String.valueOf(id);
    }
}
