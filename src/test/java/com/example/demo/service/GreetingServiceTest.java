package com.example.demo.service;

import com.example.demo.model.Greeting;
import com.example.demo.repository.GreetingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GreetingServiceTest {

    @Mock
    private GreetingRepository repository;

    @InjectMocks
    private GreetingService service;

    @Test
    void shouldReturnAllGreetings() {
        var g1 = new Greeting("Hello", "en"); g1.setId(1L);
        var g2 = new Greeting("Hola", "es"); g2.setId(2L);
        var greetings = List.of(g1, g2);
        when(repository.findAll()).thenReturn(greetings);

        var result = service.findAll();

        assertThat(result).hasSize(2);
        verify(repository).findAll();
    }

    @Test
    void shouldFindById() {
        var greeting = new Greeting("Hello", "en"); greeting.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(greeting));

        var result = service.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getMessage()).isEqualTo("Hello");
    }

    @Test
    void shouldReturnEmptyForNonExistentId() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        var result = service.findById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldCreateGreeting() {
        var input = new Greeting("Hello");
        var saved = new Greeting("Hello", "en"); saved.setId(1L);
        when(repository.save(input)).thenReturn(saved);

        var result = service.create(input);

        assertThat(result.getId()).isEqualTo(1L);
        verify(repository).save(input);
    }
}
