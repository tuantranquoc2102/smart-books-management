package com.example.books.config;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpStatusCode;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class OpenAiSummaryClient {

    private final WebClient webClient;
    private final String model;

    public OpenAiSummaryClient(
            @Value("${openai.apiKey}") String apiKey,
            @Value("${openai.model:gpt-4o-mini}") String model
    ) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("OPENAI_API_KEY is missing or blank!");
        }
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey.trim())
                .build();
        this.model = model;
    }

    public Mono<String> summarize(String chapterTitle, String text) {
        String input = text.length() > 8000 ? text.substring(0, 8000) : text;

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content",
                                "You are a concise summarizer. Output Vietnamese bullet points and a 2-3 sentence overview."),
                        Map.of("role", "user", "content",
                                "Tóm tắt CHƯƠNG: " + chapterTitle + "\n\nNội dung:\n" + input +
                                        "\n\nYêu cầu: 1) 5-8 bullet points, 2) 3 key quotes (nếu có), 3) 3 câu hỏi ôn tập ở cuối.")
                ),
                "temperature", 0.2
        );

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, resp ->
                        resp.bodyToMono(String.class).flatMap(msg ->
                                Mono.error(new RuntimeException("OpenAI 4xx: " + msg))))
                .onStatus(HttpStatusCode::is5xxServerError, resp ->
                        resp.bodyToMono(String.class).flatMap(msg ->
                                Mono.error(new RuntimeException("OpenAI 5xx: " + msg))))
                .bodyToMono(JsonNode.class)
                .map(json -> json.at("/choices/0/message/content").asText(""));
    }
}