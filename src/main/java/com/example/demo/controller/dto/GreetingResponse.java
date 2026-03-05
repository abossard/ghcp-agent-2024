package com.example.demo.controller.dto;

import com.example.demo.model.Greeting;

import java.util.List;

/**
 * Response DTO — decouples API contract from JPA entity.
 * Use the static factory {@code from()} to convert entities.
 */
public record GreetingResponse(
        Long id,
        String message,
        String language
) {
    public static GreetingResponse from(Greeting entity) {
        return new GreetingResponse(entity.getId(), entity.getMessage(), entity.getLanguage());
    }

    public static List<GreetingResponse> fromList(List<Greeting> entities) {
        return entities.stream().map(GreetingResponse::from).toList();
    }
}
