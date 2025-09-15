package com.example.books.service.impl;


import com.example.books.dto.mapper.BookMapper;
import com.example.books.dto.request.BookRequest;
import com.example.books.dto.response.BookResponse;
import com.example.books.entity.Book;
import com.example.books.repository.BookRepository;
import com.example.books.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository repository;
    private final BookMapper mapper;

    @Override
    public List<BookResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Book not found id=" + id));
    }

    @Override
    @Transactional
    public BookResponse create(BookRequest dto) {
        Book book = mapper.toEntity(dto);
        return mapper.toResponse(repository.save(book));
    }

    @Override
    @Transactional
    public BookResponse update(Long id, BookRequest dto) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found id=" + id));
        mapper.updateEntityFromDto(dto, book);
        return mapper.toResponse(repository.save(book));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Book not found id=" + id);
        }
        repository.deleteById(id);
    }
}