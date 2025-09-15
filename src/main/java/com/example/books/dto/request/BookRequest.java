package com.example.books.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {

    @NotBlank
    @Size(max = 200)
    private String title;

    @Size(max = 100)
    private String author;

    @Size(max = 50_000)          // giới hạn nội dung tùy ý
    private String content;

    @Min(1000) @Max(2030)
    private Integer year;        // đổi thành Integer cho phép null

    @NotNull
    private Status status;

    public enum Status { DRAFT, PUBLISHED }
}