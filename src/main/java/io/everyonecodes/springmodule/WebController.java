package io.everyonecodes.springmodule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class WebController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private BookService bookService;

    @GetMapping("/")
    ModelAndView index() {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("reviews", reviewService.getAll());
        return mav;
    }

    @GetMapping("/review/{id}")
    ModelAndView getOne(@PathVariable long id) {
        var review = reviewService.getOne(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find review"));
        ModelAndView mav = new ModelAndView("review");
        mav.addObject("review", review);
        return mav;
    }

    @GetMapping("/writeReview/{id}")
    String writeReview(Model model, @PathVariable long id) {
        Review review = new Review();
        review.setBook(bookService.getOne(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find book")));
        model.addAttribute("review", review);
        return "writeReview";
    }

    @PostMapping("/writeReview/{id}")
    ModelAndView writeReview(@ModelAttribute Review review, @PathVariable long id) {
        Book book = bookService.getOne(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find book"));

        review.setPublishedDate(LocalDateTime.now());
        review.setBook(book);
        reviewService.createNew(review);
        book.getReviews().add(review);

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
        model.addAttribute("review", reviewService.getOne(id));
        return "editReview";
    }

    @PostMapping("/editReview/{id}")
    ModelAndView editReview(@PathVariable long id, @ModelAttribute Review review) {
        review.setPublishedDate(LocalDateTime.now());
        reviewService.change(id, review);
        return index();
    }

    @GetMapping("/searchBooks")
    String searchBooks(@RequestParam String query, Model model) {
        List<Book> books = bookService.searchBooks(query);
        model.addAttribute("books", books);
        return "searchBooks";
    }

    @GetMapping("/book/{title}")
    ModelAndView getOneBook(@PathVariable String title) {
        ModelAndView mav = new ModelAndView("book");
        mav.addObject("book", bookService.getOneByTitle(title));
        return mav;
    }
}
