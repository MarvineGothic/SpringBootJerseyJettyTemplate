package org.example.infrastructure.event;

import lombok.RequiredArgsConstructor;
import org.example.domain.event.EventPublisher;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InternalApplicationEventPublisher implements EventPublisher {
    private final org.springframework.context.ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(Object event) {
        applicationEventPublisher.publishEvent(event);
    }
}
