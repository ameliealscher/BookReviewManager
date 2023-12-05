package io.everyonecodes.springmodule;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
public class BookService {
    private final BookRepository repository;
    private final WebClient.Builder webClientBuilder;

    public BookService(BookRepository repository, WebClient.Builder webClientBuilder) {
        this.repository = repository;
        this.webClientBuilder = webClientBuilder;
    }

    public Optional<Book> getOne(@PathVariable long id) {
        return repository.findById(id);
    }

    public Book getOneByTitle(@PathVariable String title) {
        return repository.findByTitle(title);
    }

    public List<Book> searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();

        String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=" + keyword;

        try {
            JsonNode jsonResponse = webClientBuilder.build()
                    .get()
                    .uri(apiUrl)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (jsonResponse != null) {
                JsonNode items = jsonResponse.get("items");

                if (items != null && items.isArray()) {
                    for (JsonNode item : items) {
                        JsonNode volumeInfo = item.get("volumeInfo");
                        String title = volumeInfo.get("title").asText();

                        JsonNode authorsArray = volumeInfo.get("authors");
                        List<String> authorList = new ArrayList<>();

                        if (authorsArray != null && authorsArray.isArray()) {
                            for (JsonNode authorNode : authorsArray) {
                                authorList.add(authorNode.asText());
                            }
                        } else {
                            String author = volumeInfo.path("authors").asText("N/A");
                            authorList.add(author);
                        }
                        books.add(new Book(title, authorList, List.of()));
                    }
                }
            }
        } catch (WebClientException e) {
            e.printStackTrace();
        }

        for (Book book : books) {
            if (!repository.existsByTitle(book.getTitle())) {
                repository.save(book);
            }
        }

        return books;
    }
    public Optional<Double> calculateAverageScore(long id) {
        Optional<Book> book = repository.findById(id);
        if(book.isEmpty()){
            return Optional.empty();
        }
        if (book.get().getReviews().isEmpty()) {
            return Optional.of(0.0);
        }
        double totalScore = book.get().getReviews().stream().mapToInt(Review::getScore).sum();
        return Optional.of(totalScore / book.get().getReviews().size());
    }
    public Double calculateAverageScore(String title) {
        Book book = repository.findByTitle(title);
        if (book.getReviews().isEmpty()) {
            return 0.0;
        }
        double totalScore = book.getReviews().stream().mapToInt(Review::getScore).sum();
        int scale = (int) Math.pow(10, 1);
        return (double) Math.round((totalScore / book.getReviews().size()) * scale) / scale;
    }

    public List<BookWrapper> getTop10(){
        List<Book> books = repository.findAll();
        List<BookWrapper> bookWrappers = new ArrayList<>();
        for(Book book : books) {
            bookWrappers.add(new BookWrapper(book, calculateAverageScore(book.getTitle())));
        }
        return bookWrappers.stream()
                .sorted(Comparator.comparingDouble(BookWrapper::getAverageScore).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
}
