package com.example.books.dto.mapper;

import com.example.books.dto.request.BookRequest;
import com.example.books.dto.response.BookResponse;
import com.example.books.entity.Book;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookMapper {

    Book toEntity(BookRequest dto);

    @Mapping(target = "status", source = "status")
    BookResponse toResponse(Book entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(BookRequest dto, @MappingTarget Book entity);

    /* MapStruct tự động chuyển enum Status ↔ String */
}