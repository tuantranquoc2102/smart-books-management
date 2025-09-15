package com.example.books.controller;

import com.example.books.dto.request.BookRequest;
import com.example.books.dto.response.BookResponse;
import com.example.books.repository.BookRepository;
import com.example.books.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookRepository repo;
    private final BookService bookService;

    /**
     * Get list books
     * @return
     */
    @GetMapping
    public List<BookResponse> getAll() {
        return bookService.findAll();
    }

    /**
     * Get detail book
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public BookResponse getById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    /**
     * Create new book
     * @param request
     * @return
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse create(@Valid @RequestBody BookRequest request) {
        return bookService.create(request);
    }

    /**
     * Update book
     * @param id
     * @param request
     * @return
     */
    @PutMapping("/{id}")
    public BookResponse update(@PathVariable Long id,
                               @Valid @RequestBody BookRequest request) {
        return bookService.update(id, request);
    }

    /**
     * Delete book
     * @param id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }
}