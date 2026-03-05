package com.example.demo.service;

import com.example.demo.model.Greeting;
import com.example.demo.repository.GreetingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GreetingService {

    private final GreetingRepository repository;

    public GreetingService(GreetingRepository repository) {
        this.repository = repository;
    }

    public List<Greeting> findAll() {
        return repository.findAll();
    }

    public Optional<Greeting> findById(Long id) {
        return repository.findById(id);
    }

    public List<Greeting> findByLanguage(String language) {
        return repository.findByLanguage(language);
    }

    public Greeting create(Greeting greeting) {
        return repository.save(greeting);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
