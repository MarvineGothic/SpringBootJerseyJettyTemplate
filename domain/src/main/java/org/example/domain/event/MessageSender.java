package org.example.domain.event;

import org.example.event.EventType;

public interface MessageSender {
    String EVENT_TYPE_CUSTOM_HEADER = "eventType";
    void sendMessage(EventType eventType, Object message);
}
