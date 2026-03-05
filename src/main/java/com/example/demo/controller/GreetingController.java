package com.example.demo.controller;

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
    public List<Greeting> getAll() {
        return greetingService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Greeting> getById(@PathVariable Long id) {
        return greetingService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/language/{language}")
    public List<Greeting> getByLanguage(@PathVariable String language) {
        return greetingService.findByLanguage(language);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Greeting create(@Valid @RequestBody Greeting greeting) {
        return greetingService.create(greeting);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        greetingService.delete(id);
    }
}
