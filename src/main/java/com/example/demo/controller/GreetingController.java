package com.example.demo.controller;

import com.example.demo.controller.dto.CreateGreetingRequest;
import com.example.demo.controller.dto.GreetingResponse;
import com.example.demo.model.Greeting;
import com.example.demo.service.GreetingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/greetings")
public class GreetingController {

    private final GreetingService greetingService;

    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping
    public List<GreetingResponse> getAll() {
        return GreetingResponse.fromList(greetingService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GreetingResponse> getById(@PathVariable Long id) {
        return greetingService.findById(id)
                .map(GreetingResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/language/{language}")
    public List<GreetingResponse> getByLanguage(@PathVariable String language) {
        return GreetingResponse.fromList(greetingService.findByLanguage(language));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GreetingResponse create(@Valid @RequestBody CreateGreetingRequest request) {
        var greeting = new Greeting(request.message(), request.languageOrDefault());
        return GreetingResponse.from(greetingService.create(greeting));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        greetingService.delete(id);
    }
}
