package org.example.eventHandler;

import lombok.RequiredArgsConstructor;
import org.example.event.UserCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationServiceHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceHandler.class);

    @EventListener
    @Async
    public void handle(UserCreatedEvent userCreatedEvent)  throws InterruptedException {
        LOGGER.info("\nNotificationService: Sending user notification  {}", Thread.currentThread().getName());
    }
}
