package org.example.event;

public record SqsMessage (
        String ulid,
        EventType eventType,
        Object object
) implements Event {
}
