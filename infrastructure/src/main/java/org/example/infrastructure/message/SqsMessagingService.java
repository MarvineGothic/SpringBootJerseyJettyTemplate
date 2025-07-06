package org.example.infrastructure.message;

import com.github.f4b6a3.ulid.UlidCreator;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import org.example.domain.event.MessageSender;
import org.example.event.EventType;
import org.example.domain.event.SqsMessage;
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
public class SqsMessagingService implements MessageSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqsMessagingService.class);
    @Value("${events.queue.myQueue}")
    private String QUEUE_NAME;

    private final SqsTemplate sqsTemplate;

    @SqsListener("${events.queue.myQueue}")
    public void message(Message<SqsMessage> message,
                        @Header(EVENT_TYPE_CUSTOM_HEADER) EventType eventType,
                        @Header(SQS_APPROXIMATE_FIRST_RECEIVE_TIMESTAMP) Long firstReceive) {
        LOGGER.info("\nReceived message {} with event type {}. First received at approximately {}.", message.getPayload(), eventType, firstReceive);
    }

    @Override
    public void sendMessage(EventType eventType, Object o) {
        LOGGER.info("\nSending event");
        SqsMessage event = new SqsMessage(UlidCreator.getUlid().toString(), eventType, o);
        var headers = Map.<String, Object>of(EVENT_TYPE_CUSTOM_HEADER, eventType);
        sqsTemplate.send(to -> to.queue(QUEUE_NAME).payload(event).headers(headers));
    }
}
