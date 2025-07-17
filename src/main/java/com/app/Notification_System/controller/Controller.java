package com.app.Notification_System.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Notification_System.model.EventRequests;
import com.app.Notification_System.service.EventTriggerService;

@RestController
@RequestMapping("/api")
public class Controller 
{
	 @Autowired
	 private EventTriggerService eventService;
	 
    @PostMapping("/events")
    public ResponseEntity<?> addEvent(@RequestBody EventRequests request)
    {
        try 
        {
            EventRequests event = eventService.eventTrigger(request);
            return ResponseEntity.ok(
            Map.of(
                "eventType", event.getEventType(),
                "message", "Event accepted for processing."
            ));
        } 
        catch (IllegalArgumentException ex) 
        {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }
    
    @PostMapping("/event-status")
    public ResponseEntity<String> receiveStatus(@RequestBody Map<String, Object> body) {
        System.out.println("Received callback: " + body);
        return ResponseEntity.ok("Callback received successfully.");
    }
}
