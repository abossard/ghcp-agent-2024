package com.example.demo.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a new Greeting.
 * Separates the API contract from the JPA entity.
 */
public record CreateGreetingRequest(
        @NotBlank(message = "Message is required")
        @Size(max = 255, message = "Message must be at most 255 characters")
        String message,

        @Size(max = 10, message = "Language code must be at most 10 characters")
        String language
) {
    public String languageOrDefault() {
        return language != null && !language.isBlank() ? language : "en";
    }
}
