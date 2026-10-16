package com.example.ezyshop.service;

import com.example.ezyshop.entity.DocumentChunk;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RagService {

    private final RetrievalService retrievalService;

    private final AiService aiService;

    public JsonNode ask(String question) {

        // 1. Retrieve
        List<DocumentChunk> chunks =
                retrievalService.search(question);

        // 2. Build context
        String context = chunks.stream()
                .map(DocumentChunk::getContent)
                .collect(Collectors.joining("\n\n"));

        // 3. Build prompt
        String prompt = """
                Answer using only the context.

                Context:
                %s

                Question:
                %s

                If the answer is not in the context,
                say you don't have enough information.
                """.formatted(context, question);

        // 4. Generate
        return aiService.chat(prompt);
    }
}
