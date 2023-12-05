package io.everyonecodes.springmodule;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BookController {
    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping("/search")
    List<Book> searchBooks(@RequestParam String q) {
        return service.searchBooks(q);
    }

    @GetMapping("/book/{id}")
    Book getOne(@PathVariable long id) {
        return service.getOne(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find book"));
    }

    @GetMapping("/averageScore/{id}")
    Double getAverageScore(@PathVariable long id) {
        return service.calculateAverageScore(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find book"));
    }

    @GetMapping("/top")
    List<Book> getTop10() {
        List<Book> books = new ArrayList<>();
        List<BookWrapper> bookWrappers = service.getTop10();
        for(BookWrapper bookWrapper : bookWrappers){
            books.add(bookWrapper.getBook());
        }
        return books;
    }
}
