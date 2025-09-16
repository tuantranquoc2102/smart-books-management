package com.example.books.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chapter_summaries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChapterSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookId;
    private int chapterIndex;

    @Column(length = 500)   // tiêu đề chương ngắn
    private String chapterTitle;

    @Column(columnDefinition = "TEXT") // tóm tắt
    private String summary;

    @Column(columnDefinition = "TEXT") // trích đoạn gốc đã dùng để tóm tắt
    private String rawText;
}
