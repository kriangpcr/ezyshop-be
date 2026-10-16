//package com.example.ezyshop.controller;
//
//import com.example.ezyshop.dto.ai.ChatRequest;
//import com.example.ezyshop.dto.ai.DocumentRequest;
//import com.example.ezyshop.service.AiService;
//import com.example.ezyshop.service.DocumentService;
//import com.example.ezyshop.service.RagService;
//import org.springframework.http.ResponseEntity;
//import tools.jackson.databind.JsonNode;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/ai")
//@RequiredArgsConstructor
//public class AiController {
//
//    private final AiService aiService;
//
//    private final DocumentService documentService;
//    private final RagService ragService;
//
//    @PostMapping("/chat")
//    public JsonNode chat(
//            @RequestBody ChatRequest request
//    ) {
//        return aiService.chat(request.message());
//    }
//
//    @PostMapping("/rag/document")
//    public String saveDocument(
//            @RequestBody DocumentRequest request
//    ) {
//        return documentService.ingest( request.message(),request.documentName());
//    }
//
//    @PostMapping("/rag/chat")
//    public JsonNode ragRetrieve(
//            @RequestBody String message
//    ) {
//        return ragService.ask(message);
//    }
//}
