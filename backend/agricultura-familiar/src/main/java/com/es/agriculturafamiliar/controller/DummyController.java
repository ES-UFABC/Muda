package com.es.agriculturafamiliar.controller;

import com.es.agriculturafamiliar.event.DummyEvent;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController()
@RequestMapping("/api/dummy")
@AllArgsConstructor
public class DummyController {

    private final ApplicationEventPublisher applicationEventPublisher;
    
    @GetMapping
    public ResponseEntity<?> hullo() {
        return ResponseEntity.ok("hullo");
    }

    @GetMapping("/email")
    public ResponseEntity<?> sendEMail(@RequestParam String name, @RequestParam String toEmail) {
        applicationEventPublisher.publishEvent(new DummyEvent(name, toEmail));
        return ResponseEntity.ok().build();
    }
}
