package com.example.books.repository;

import com.example.books.entity.ChapterSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChapterSummaryRepository extends JpaRepository<ChapterSummary, Long> {
    List<ChapterSummary> findByBookIdOrderByChapterIndexAsc(Long bookId);
}
