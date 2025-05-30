package org.example.service;

import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);
    private static final String QUEUE_NAME = "myQueue";

    private final SqsAsyncClient sqsAsyncClient;

    @SqsListener(QUEUE_NAME)
    public void listen(String message) {
        LOGGER.info("\nReceived message: " + message);
    }

    public void sendMessage() {
        LOGGER.info("Sending message");
        SqsTemplate sqsTemplate = SqsTemplate.newTemplate(sqsAsyncClient);
        sqsTemplate.send(QUEUE_NAME, "payload");
    }
}
