package com.example.books.entity;

import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter;

@Entity @Table(name="books")
@Getter @Setter
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=200)
    private String title;

    @Column(length=100)
    private String author;

    @Lob
    private String content;

    @Column(name="published_year")
    private Integer year;

    @Enumerated(EnumType.STRING)
    private Status status = Status.DRAFT;

    public enum Status { DRAFT, PUBLISHED }
}
