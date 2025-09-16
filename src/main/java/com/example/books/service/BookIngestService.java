package com.example.books.service;

import com.example.books.component.PdfChapterExtractor;
import com.example.books.config.OpenAiSummaryClient;
import com.example.books.entity.ChapterSummary;
import com.example.books.repository.ChapterSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookIngestService {
    private final PdfChapterExtractor extractor;
    private final OpenAiSummaryClient openAi;
    private final ChapterSummaryRepository chapterSummaryRepository;

    @Transactional
    public List<ChapterSummary> process(Long bookId, InputStream pdf) throws IOException {
        var chapters = extractor.extractChapters(pdf);
        List<ChapterSummary> results = new ArrayList<>();

        for (var c : chapters) {
            String summary = openAi.summarize(c.getTitle(), c.getText()).block(); // MVP: sync
            ChapterSummary cs = new ChapterSummary();
            cs.setBookId(bookId);
            cs.setChapterIndex(c.getIndex());
            cs.setChapterTitle(c.getTitle());
            cs.setSummary(summary);
            cs.setRawText(trimForStorage(c.getText()));
            results.add(chapterSummaryRepository.save(cs));
        }
        return results;
    }

    private String trimForStorage(String s) {
        return s.length() > 20000 ? s.substring(0, 20000) : s;
    }
}
