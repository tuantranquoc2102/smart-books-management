package com.example.books.controller;

import com.example.books.dto.request.BookRequest;
import com.example.books.dto.response.BookResponse;
import com.example.books.entity.ChapterSummary;
import com.example.books.repository.BookRepository;
import com.example.books.repository.ChapterSummaryRepository;
import com.example.books.service.BookIngestService;
import com.example.books.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final BookIngestService ingest;
    private final ChapterSummaryRepository chapterSummaryRepository;

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

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file,
                                    @RequestParam(value="bookId", required=false) Long bookId) throws IOException {
        if (file.isEmpty()) return ResponseEntity.badRequest().body("File trống");
        if (bookId == null) bookId = System.currentTimeMillis(); // MVP tạm

        try (InputStream is = file.getInputStream()) {
            ingest.process(bookId, is);
        }
        return ResponseEntity.ok(Map.of("bookId", bookId, "status", "processing_done"));
    }

    @GetMapping("/{bookId}/summaries")
    public List<ChapterSummary> summaries(@PathVariable Long bookId) {
        return chapterSummaryRepository.findByBookIdOrderByChapterIndexAsc(bookId);
    }
}