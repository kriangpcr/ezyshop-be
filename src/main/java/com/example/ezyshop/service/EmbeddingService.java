package com.example.ezyshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmbeddingService {

    private final RestClient restClient;

    @Value("${gemini.api-key}")
    private String apiKey;

    public float[] embed(String text) {

        Map<String, Object> request = Map.of(
                "content", Map.of(
                        "parts", List.of(
                                Map.of(
                                        "text", text
                                )
                        )
                ),
                "output_dimensionality",768
        );

        JsonNode response = restClient
                .post()
                .uri(
                        "https://generativelanguage.googleapis.com/v1beta/models/gemini-embedding-2:embedContent"
                )
                .header("x-goog-api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(JsonNode.class);
        return parseEmbedding(response);
    }

    private float[] parseEmbedding(JsonNode response) {

        JsonNode values =
                response
                        .get("embedding")
                        .get("values");

        float[] result = new float[768];

        for (int i = 0; i < values.size(); i++) {
            result[i] = values.get(i).floatValue();
        }

        return result;
    }
}
