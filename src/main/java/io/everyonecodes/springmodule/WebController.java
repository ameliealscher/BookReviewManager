package io.everyonecodes.springmodule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class WebController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private BookService bookService;

    @GetMapping("/")
    ModelAndView index() {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("top10", bookService.getTop10());;
        return mav;
    }

    @GetMapping("/review/{id}")
    ModelAndView getOne(@PathVariable long id) {
        var review = reviewService.getOne(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find review"));
        ModelAndView mav = new ModelAndView("review");
        mav.addObject("review", review);
        mav.addObject("score", review.getScore());
        return mav;
    }

    @GetMapping("/writeReview/{id}")
    String writeReview(Model model, @PathVariable long id) {
        Review review = new Review();
        Book book = bookService.getOne(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find book"));
        review.setBook(book);
        model.addAttribute("review", review);
        model.addAttribute("bookTitle", book.getTitle());
        return "writeReview";
    }

    @PostMapping("/writeReview/{id}")
    ModelAndView writeReview(@ModelAttribute Review review, @PathVariable long id) {
        Book book = bookService.getOne(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find book"));

        review.setPublishedDate(LocalDateTime.now());
        review.setBook(book);

        reviewService.createNew(review);

        ModelAndView mav = new ModelAndView("book");
        mav.addObject("book", book);
        return mav;
    }

    @DeleteMapping("/review/{id}")
    ModelAndView delete(@PathVariable long id) {
        reviewService.delete(id);
        return index();
    }

    @GetMapping("/editReview/{id}")
    String editReview(@PathVariable long id, Model model) {
        Review review = reviewService.getOne(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find review"));
        model.addAttribute("review", review);
        model.addAttribute("bookTitle", review.getBook().getTitle());
        return "editReview";
    }

    @PostMapping("/editReview/{id}")
    ModelAndView editReview(@PathVariable long id, @ModelAttribute Review review) {
        review.setPublishedDate(LocalDateTime.now());
        review = reviewService.change(id, review);

        ModelAndView mav = new ModelAndView("book");
        mav.addObject("book", review.getBook());
        return mav;
    }

    @GetMapping("/searchBooks")
    String searchBooks(@RequestParam String query, Model model) {
        List<Book> books = bookService.searchBooks(query);
        List<BookWrapper> bookWrappers = new ArrayList<>();

        for(Book book : books){
            Double averageScore = bookService.calculateAverageScore(book.getTitle());
                bookWrappers.add(new BookWrapper(book, averageScore));
            }
        model.addAttribute("booksAndScores", bookWrappers);

        return "searchBooks";
    }

    @GetMapping("/book/{title}")
    ModelAndView getOneBook(@PathVariable String title) {
        ModelAndView mav = new ModelAndView("book");
        mav.addObject("book", bookService.getOneByTitle(title));
        return mav;
    }
}
