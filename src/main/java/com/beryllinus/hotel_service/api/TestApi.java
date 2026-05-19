package com.beryllinus.hotel_service.api;

import org.springframework.web.reactive.function.client.WebClient;

public class TestApi {

    private final WebClient webClient;

    public TestApi(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://api.example.com").build();
    }

    public String getUser() {
        return webClient.get()
                .uri("/users")
                .retrieve()
                .bodyToMono(String.class)
                .block(); // sync
    }
}
