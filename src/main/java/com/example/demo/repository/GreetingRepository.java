package com.example.demo.repository;

import com.example.demo.model.Greeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GreetingRepository extends JpaRepository<Greeting, Long> {
    List<Greeting> findByLanguage(String language);
}
