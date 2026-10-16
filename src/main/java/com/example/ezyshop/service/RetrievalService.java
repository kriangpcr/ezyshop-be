package com.example.ezyshop.service;

import com.example.ezyshop.entity.DocumentChunk;
import com.example.ezyshop.repository.DocumentChunkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RetrievalService {
    private final EmbeddingService embeddingService;
    private final DocumentChunkRepository repository;
    public List<DocumentChunk> search(
            String question
    ) {

        float[] queryEmbedding =
                embeddingService.embed(question);
        String embedding = java.util.Arrays.toString(queryEmbedding);
        return repository.similaritySearch(
                embedding,
                5
        );
    }
}
