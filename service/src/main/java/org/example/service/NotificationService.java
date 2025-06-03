package org.example.service;

import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import org.example.event.EventDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.Map;

import static io.awspring.cloud.sqs.listener.SqsHeaders.MessageSystemAttributes.SQS_APPROXIMATE_FIRST_RECEIVE_TIMESTAMP;

@Service
@RequiredArgsConstructor
public class NotificationService {
    public static final String EVENT_TYPE_CUSTOM_HEADER = "eventType";

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);
    @Value("${events.queue.myQueue}")
    private String QUEUE_NAME;

    private final SqsTemplate sqsTemplate;

    @SqsListener("${events.queue.myQueue}")
    public void message(Message<EventDto> message,
                        @Header(EVENT_TYPE_CUSTOM_HEADER) String eventType,
                        @Header(SQS_APPROXIMATE_FIRST_RECEIVE_TIMESTAMP) Long firstReceive) {
        LOGGER.info("\nReceived message {} with event type {}. First received at approximately {}.", message.getPayload(), eventType, firstReceive);
    }

    public void sendMessage(String eventType, Object o) {
        LOGGER.info("\nSending event");
        EventDto event = new EventDto(eventType, o);
        var headers = Map.<String, Object>of(EVENT_TYPE_CUSTOM_HEADER, "UserCreatedEvent");
        sqsTemplate.send(to -> to.queue(QUEUE_NAME).payload(event).headers(headers));
    }
}
