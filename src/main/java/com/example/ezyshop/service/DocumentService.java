package com.example.ezyshop.service;

import com.example.ezyshop.entity.DocumentChunk;
import com.example.ezyshop.repository.DocumentChunkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final EmbeddingService embeddingService;
    private final DocumentChunkRepository repository;

    public String ingest(String document,
                         String documentName) {

        List<String> chunks =
                chunk(document);

        for (int i = 0; i < chunks.size(); i++) {

            String content = chunks.get(i);

            float[] embedding =
                    embeddingService.embed(content);

            DocumentChunk documentChunk = new DocumentChunk();
            documentChunk.setContent(content);
            documentChunk.setEmbedding(embedding);
            documentChunk.setPageNumber(i);
            documentChunk.setDocumentName(documentName);
            repository.save(documentChunk);
        }
        return "Success";
    }
    private List<String> chunk(String document) {

        int chunkSize = 500;
        int overlap = 100;

        List<String> chunks = new ArrayList<>();

        int start = 0;

        while (start < document.length()) {

            int end = Math.min(
                    start + chunkSize,
                    document.length()
            );

            String chunk = document.substring(start, end);

            chunks.add(chunk);

            if (end == document.length()) {
                break;
            }

            start = end - overlap;
        }

        return chunks;
    }
}
