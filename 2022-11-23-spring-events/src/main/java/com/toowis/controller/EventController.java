package com.toowis.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toowis.events.CustomEventPublisher;
import com.toowis.events.GenericEventPublisher;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class EventController {
    
    private final CustomEventPublisher customEventPublisher;
    private final GenericEventPublisher<String> genericEventPublisher;

    @GetMapping("/event")
    public String event(@RequestParam String message) {
        customEventPublisher.publish(message);
        return "finished";
    }
    
    @GetMapping("/event/generic")
    public String event(@RequestParam String message, @RequestParam boolean success) {
        genericEventPublisher.publish(message, success);
        return "finished";
    }
}
