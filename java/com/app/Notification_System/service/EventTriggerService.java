package com.app.Notification_System.service;

import java.util.EnumMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.app.Notification_System.model.EventRequests;
import com.app.Notification_System.model.EventType;
import com.app.Notification_System.processor.EventProcessor;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class EventTriggerService 
{
	private final EnumMap<EventType, BlockingQueue<EventRequests>> queues = new EnumMap<>(EventType.class);
    private final EnumMap<EventType, EventProcessor> processors = new EnumMap<>(EventType.class);
    private final ExecutorService executor = Executors.newFixedThreadPool(3);
    
    @PostConstruct
    public void init() 
    {
        for (EventType type : EventType.values())
        {
            BlockingQueue<EventRequests> queue = new LinkedBlockingQueue<>();
            queues.put(type, queue);
            EventProcessor processor = new EventProcessor(type, queue);
            processors.put(type, processor);
            executor.submit(processor);
        }
    }

    public EventRequests eventTrigger(EventRequests request) 
    {
        EventType type = request.getEventType();
        
        if (type == null || request.getPayload() == null || request.getCallbackUrl() == null)
        {
            throw new IllegalArgumentException("Invalid request data.");
        }
        
        EventRequests event = new EventRequests(type, request.getPayload(), request.getCallbackUrl());
        queues.get(type).offer(event);
        
        return event;
    }

    @PreDestroy
    public void shutdown() 
    {
        processors.values().forEach(EventProcessor::stop);
        executor.shutdown();
        
        try 
        {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } 
        catch (InterruptedException e) 
        {
            Thread.currentThread().interrupt();
        }
    }
}
