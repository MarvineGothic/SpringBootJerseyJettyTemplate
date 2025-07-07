package org.example.application.service.eventHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.event.UserCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceHandler {

    private final ObjectMapper objectMapper;

    @EventListener
    @Async
    public void handle(UserCreatedEvent userCreatedEvent) throws JsonProcessingException {
        var writer = objectMapper.writer().withDefaultPrettyPrinter();

        log.info("\nNotificationService: Sending user notification {}, thread {}",
                objectMapper.writeValueAsString(userCreatedEvent.event()), Thread.currentThread().getName());

        log.info("\nNotificationService: Sending user notification 2 {}, thread {}",
                writer.writeValueAsString(userCreatedEvent.event()), Thread.currentThread().getName());
    }
}
