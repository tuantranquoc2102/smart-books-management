package com.example.books.service;

import com.example.books.dto.request.BookRequest;
import com.example.books.dto.response.BookResponse;

import java.util.List;

public interface BookService {
    List<BookResponse> findAll();
    BookResponse findById(Long id);
    BookResponse create(BookRequest dto);
    BookResponse update(Long id, BookRequest dto);
    void delete(Long id);
}