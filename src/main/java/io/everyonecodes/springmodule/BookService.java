package io.everyonecodes.springmodule;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.Optional;

@Service
public class BookService {
    private final BookRepository repository;
    private final WebClient.Builder webClientBuilder;

    public BookService(BookRepository repository, WebClient.Builder webClientBuilder) {
        this.repository = repository;
        this.webClientBuilder = webClientBuilder;
    }

    @Value("${google.books.api.key}")
    private String apiKey;

    public Optional<Book> getOne(@PathVariable long id) {
        return repository.findById(id);
    }

    public Book getOneByTitle(@PathVariable String title) {
        return repository.findByTitle(title);
    }

    public List<Book> searchBooks(String keyword) {
            String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=" + keyword + "&key=" + apiKey;

            List<Book> books = new ArrayList<>();

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
                if (!repository.findAll().contains(getOneByTitle(book.getTitle()))) {
                    repository.save(book);
                }
            }

            return books;
        }
    }
