package com.example.books.repository;

import com.example.books.entity.Book;
import com.example.books.entity.Book.Status;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE (:author IS NULL OR b.author = :author)")
    Page<Book> findByAuthor(@Param("author") String author, Pageable pageable);

    Page<Book> findByStatus(Status status, Pageable pageable);

    @Modifying
    @Query("UPDATE Book b SET b.title = :title WHERE b.id = :id")
    int updateTitle(@Param("id") Long id, @Param("title") String title);
}