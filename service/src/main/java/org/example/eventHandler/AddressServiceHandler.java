package org.example.eventHandler;

import lombok.RequiredArgsConstructor;
import org.example.database.entity.Address;
import org.example.database.entity.User;
import org.example.event.UserCreatedEvent;
import org.example.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressServiceHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressServiceHandler.class);

    private final AddressService addressService;

    @EventListener
    @Async
    public void handle(UserCreatedEvent userCreatedEvent) throws InterruptedException {
        LOGGER.info("\nAddressService: Creating user address {}", Thread.currentThread().getName());
        User user = (User) userCreatedEvent.getEvent();
        var address = Address.builder()
                .address("Copenhagen")
                .user(user)
                .build();
        addressService.addAddress(address);
    }
}
