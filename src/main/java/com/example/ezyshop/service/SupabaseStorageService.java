package com.example.ezyshop.service;

import com.example.ezyshop.repository.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class SupabaseStorageService implements StorageService {
    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String bucket;

    private final RestClient restClient;

    public SupabaseStorageService(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    @Override
    public String upload(MultipartFile file) {
        String fileName =
                UUID.randomUUID() + "_" + file.getOriginalFilename();

        restClient.post()
                .uri(supabaseUrl
                        + "/storage/v1/object/"
                        + bucket
                        + "/"
                        + fileName)
                .header("Authorization", "Bearer " + supabaseKey)
                .header("apikey", supabaseKey)
                .header("Content-Type", file.getContentType())
                .body(file.getResource())
                .retrieve()
                .toBodilessEntity();

        return fileName;
    }

    public String getPublicUrl(String fileName) {
        return supabaseUrl
                + "/storage/v1/object/public/"
                + bucket
                + "/"
                + fileName;
    }

    @Override
    public void delete(String fileName) {
        restClient.delete()
                .uri(supabaseUrl
                        + "/storage/v1/object/"
                        + bucket
                        + "/"
                        + fileName)
                .header("Authorization", "Bearer " + supabaseKey)
                .header("apikey", supabaseKey)
                .retrieve()
                .toBodilessEntity();
    }
}
