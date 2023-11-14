package io.everyonecodes.springmodule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Controller
public class WebController {

    @Autowired
    private ReviewService service;

    @GetMapping("/example")
    String example() {
        return "example.html";
    }

    @GetMapping("/")
    ModelAndView index() {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("reviews", service.getAll());
        return mav;
    }

    @GetMapping("/review/{id}")
    ModelAndView getOne(@PathVariable long id) {
        var review = service.getOne(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find post"));
        ModelAndView mav = new ModelAndView("review");
        mav.addObject("review", review);
        return mav;
    }
    @GetMapping("/writeReview")
    String writeReview(Model model) {
        model.addAttribute("review", new Review());
        return "writeReview";
    }
    @PostMapping("/writeReview")
    ModelAndView writeReview(@ModelAttribute Review review){
        review.setPublishedDate(LocalDateTime.now());
        service.createNew(review);
        return index();
    }
    @DeleteMapping("/review/{id}")
    ModelAndView delete(@PathVariable long id) {
        service.delete(id);
        return index();
    }
    @GetMapping("/editReview/{id}")
    String editReview(@PathVariable long id, Model model) {
        model.addAttribute("review", service.getOne(id));
        return "editReview";
    }
    @PostMapping("/editReview/{id}")
    ModelAndView editReview(@PathVariable long id, @ModelAttribute Review review){
        review.setPublishedDate(LocalDateTime.now());
        service.change(id, review);
        return index();
    }
}
