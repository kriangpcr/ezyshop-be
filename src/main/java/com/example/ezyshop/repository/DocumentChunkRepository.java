package com.example.ezyshop.repository;

import com.example.ezyshop.entity.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, UUID> {
    @Query(value = """
    SELECT
        id,
        content,
        embedding,
        document_name,
        page_number
    FROM ezyshop.document_chunks
    ORDER BY embedding <=> CAST(:embedding AS vector)
    LIMIT :limit
    """,
            nativeQuery = true)
    List<DocumentChunk> similaritySearch(
            @Param("embedding") String embedding,
            @Param("limit") int limit
    );
}
