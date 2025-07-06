package org.example.application.service.eventHandler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.domain.entity.UserEntity;
import org.example.domain.event.UserCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressServiceHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressServiceHandler.class);

    @EventListener
    @Async
    @Transactional
    public void handle(UserCreatedEvent userCreatedEvent) {
        LOGGER.info("\nAddressService: Creating user address {}", Thread.currentThread().getName());
        UserEntity user = (UserEntity) userCreatedEvent.event();
    }
}
