package com.app.Notification_System.processor;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

import org.springframework.web.reactive.function.client.WebClient;

import com.app.Notification_System.model.EventRequests;
import com.app.Notification_System.model.EventType;

public class EventProcessor implements Runnable
{
	private final EventType eventType;
    private final BlockingQueue<EventRequests> queue;
    private volatile boolean running = true;
    private final WebClient webClient = WebClient.create();
    private final Random random = new Random();

    public EventProcessor(EventType eventType, BlockingQueue<EventRequests> queue)
    {
        this.eventType = eventType;
        this.queue = queue;
    }

    @Override
    public void run() 
    {
        while (running || !queue.isEmpty()) 
        {
            try
            {
                EventRequests event = (EventRequests) queue.poll();
                
                if (event == null) continue;

                int delay = switch (eventType) {
                    case EMAIL -> 5000;
                    case SMS -> 3000;
                    case PUSH -> 2000;
                };
                
                Thread.sleep(delay);

                boolean failed = random.nextInt(10) == 0;
                String processedAt = Instant.now().toString();

                if (failed) 
                {
                    webClient.post()
                        .uri(event.getCallbackUrl())
                        .bodyValue(
	                        Map.of(
	                            "status", "FAILED",
	                            "eventType", eventType,
	                            "errorMessage", "Simulated processing failure",
	                            "processedAt", processedAt
                        ))
                        .retrieve()
                        .bodyToMono(String.class)
                        .subscribe();
                } 
                else 
                {
                    webClient.post()
                        .uri(event.getCallbackUrl())
                        .bodyValue(
                        		Map.of(
		                            "status", "COMPLETED",
		                            "eventType", eventType,
		                            "processedAt", processedAt
                        ))
                        .retrieve()
                        .bodyToMono(String.class)
                        .subscribe();
                }
            } 
            catch (InterruptedException e) 
            {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stop()
    {
        this.running = false;
    }
}
