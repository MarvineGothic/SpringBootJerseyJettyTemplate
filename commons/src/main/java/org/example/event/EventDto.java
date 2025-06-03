package org.example.event;

public record EventDto(
        String type,
        Object object
) {
}
