package org.example.domain.event;

import org.example.event.Event;
import org.example.event.EventType;

public record SqsMessage (
        String ulid,
        EventType eventType,
        Object object
) implements Event {
}
