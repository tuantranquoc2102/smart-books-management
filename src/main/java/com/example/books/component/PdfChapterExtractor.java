package com.example.books.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PdfChapterExtractor {

    public List<ChapterChunk> extractChapters(InputStream pdf) throws IOException {
        try (PDDocument doc = PDDocument.load(pdf)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String all = stripper.getText(doc);

            // 1) tìm heading dạng Chương/Chapter
            List<ChapterChunk> chunks = splitByRegex(all, "(?im)^(?:chương|chapter)\\s+\\d+[^\\n]*$");
            if (chunks.size() >= 2) return chunks;

            // 2) fallback heading IN HOA
            chunks = splitByRegex(all, "(?m)^[A-Z][A-Z0-9 \\-]{5,}$");
            if (chunks.size() >= 2) return chunks;

            // 3) fallback cố định độ dài
            return splitEveryNChars(all, 8000); // ~8k chars/chunk
        }
    }

    private List<ChapterChunk> splitByRegex(String text, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);

        List<Integer> idx = new ArrayList<>();
        while (m.find()) idx.add(m.start());
        if (idx.isEmpty()) return List.of();

        List<ChapterChunk> out = new ArrayList<>();
        for (int i = 0; i < idx.size(); i++) {
            int start = idx.get(i);
            int end = (i + 1 < idx.size()) ? idx.get(i + 1) : text.length();
            String segment = text.substring(start, end).trim();

            String title = firstLine(segment);
            out.add(new ChapterChunk(i, title, segment));
        }
        return out;
    }

    private List<ChapterChunk> splitEveryNChars(String text, int n) {
        List<ChapterChunk> out = new ArrayList<>();
        int i = 0; int idx = 0;
        while (i < text.length()) {
            int end = Math.min(i + n, text.length());
            String seg = text.substring(i, end).trim();
            out.add(new ChapterChunk(idx++, "Chunk " + idx, seg));
            i = end;
        }
        return out;
    }

    private String firstLine(String s) {
        int nl = s.indexOf('\n');
        return (nl > 0) ? s.substring(0, nl).trim() : s;
    }

    @Data
    @AllArgsConstructor
    public static class ChapterChunk {
        private int index;
        private String title;
        private String text;
    }
}
