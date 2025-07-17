package com.app.Notification_System.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import com.app.Notification_System.model.EventRequests;
import com.app.Notification_System.model.EventType;

@SpringBootTest
public class EventServiceTest 
{
	@Autowired
	private EventTriggerService eventService;

    //Success
    @Test
    void testValidEmailEvent() 
    {
        EventRequests request = new EventRequests();
        request.setEventType(EventType.EMAIL);
        request.setPayload(Map.of(
                "recipient", "test@example.com",
                "message", "Welcome!"
        ));
        request.setCallbackUrl("http://localhost:8080/api/event-status");

        EventRequests result = eventService.eventTrigger(request);

        assertNotNull(result);
        assertEquals(EventType.EMAIL, result.getEventType());
    }

    //Missing event type
    @Test
    void testMissingEventTypeThrowsException() 
    {
        EventRequests request = new EventRequests();
        request.setPayload(Map.of("recipient", "test@example.com", "message", "Hello"));
        request.setCallbackUrl("http://localhost:8080/api/event-status");

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            eventService.eventTrigger(request);
        });

        assertEquals("Missing required fields", ex.getMessage());
    }

    //Missing callback URL
    @Test
    void testMissingCallbackUrlThrowsException() 
    {
        EventRequests request = new EventRequests();
        request.setEventType(EventType.EMAIL);
        request.setPayload(Map.of("recipient", "test@example.com", "message", "Hello"));
        request.setCallbackUrl(null);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            eventService.eventTrigger(request);
        });

        assertEquals("Missing required fields", ex.getMessage());
    }

    //Missing Payload
    @Test
    void testMissingPayloadThrowsException() 
    {
        EventRequests request = new EventRequests();
        request.setEventType(EventType.EMAIL);
        request.setPayload(null);
        request.setCallbackUrl("http://localhost");

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            eventService.eventTrigger(request);
        });

        assertEquals("Missing required fields", ex.getMessage());
    }
    
    //Random Failure Simulation
    @Test
    void testRandomFailureSimulationDoesNotCrash() throws InterruptedException 
    {
        for (int i = 0; i < 20; i++)
        {
            EventRequests request = new EventRequests();
            request.setEventType(EventType.PUSH);
            request.setPayload(Map.of("deviceId", "abc123", "message", "Test push " + i));
            request.setCallbackUrl("http://localhost:8080/api/event-status");
            eventService.eventTrigger(request);
        }

        Thread.sleep(12000);
    }

    //Graceful Shutdown
    @Test
    void testGracefulShutdown() 
    {
        assertDoesNotThrow(() -> eventService.shutdown());
    }
}
