package com.example.ezyshop.service;

import com.example.ezyshop.dto.ai.GeminiRequest;
import com.example.ezyshop.dto.product.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {

    private final RestClient restClient;

    private final ProductService productService;

    private final ObjectMapper objectMapper;
    @Value("${gemini.api-key}")
    private String apiKey;

    public JsonNode chat(String message) {

        Map<String, Object> request = Map.of(
                "model", "gemini-3.5-flash-lite",
                "input", message,
                "tools", getTools()
        );

        JsonNode response = callApi(request);
        for (int i = 0; i < 5; i++) {
            for (JsonNode step  : response.get("steps")){

                if ("model_output".equals(step.get("type").asString())) {

                    JsonNode content = step.get("content");
                    for (JsonNode item : content) {

                        if ("text".equals(item.get("type").asString())) {
                            return item;
                        }
                    }
                }

                if ("function_call".equals(step.get("type").asString())) {
                    String functionName = step.get("name").asString();

                    if ("get_all_products".equals(functionName)){
                        List<ProductResponse> products = productService.getAllProducts();
                        String productJson =
                                objectMapper.writeValueAsString(products);
                        Map<String, Object> requestFunc = Map.of(
                                "model", "gemini-3.5-flash-lite",
                                "previous_interaction_id", response.get("id").asString(),
                                "input", List.of(
                                        Map.of(
                                                "type", "function_result",
                                                "name", step.get("name").asString(),
                                                "call_id", step.get("id").asString(),
                                                "result", List.of(
                                                        Map.of(
                                                                "type", "text",
                                                                "text", productJson
                                                        )
                                                )
                                        )
                                )
                        );
                        response = callApi(requestFunc);
                        break;
                    }
                }
            }
        }

        throw new RuntimeException("No text output from Gemini");
    }

    private  List<Map<String, Object>> getTools(){
        return List.of(  Map.of(
                "type", "function",
                "name", "search_products",
                "description", "Search products by keyword",
                "parameters", Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "keyword", Map.of(
                                        "type", "string",
                                        "description", "Product keyword"
                                )
                        ),
                        "required", List.of("keyword")
                )
        ),
        Map.of(
                "type", "function",
                "name", "get_all_products",
                "description", "Get all products"
        )
    ); }

    public JsonNode callApi(Map<String, Object> request){
        return restClient
                .post()
                .uri("https://generativelanguage.googleapis.com/v1beta/interactions")
                .header("x-goog-api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(JsonNode.class);
    }
}
