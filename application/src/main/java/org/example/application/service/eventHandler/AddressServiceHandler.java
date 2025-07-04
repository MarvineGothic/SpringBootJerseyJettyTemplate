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

//    private final AddressService addressService;

//    private final UserRepository userRepository;

    @EventListener
    @Async
    @Transactional
    public void handle(UserCreatedEvent userCreatedEvent) {
        LOGGER.info("\nAddressService: Creating user address {}", Thread.currentThread().getName());
        UserEntity user = (UserEntity) userCreatedEvent.event();
//        var address = Address.builder()
//                .city("Copenhagen")
//                .street("Mattheusgade")
//                .house("87")
//                .country("Denmark")
//                .users(List.of(user))
//                .build();
//        address = addressService.addAddress(address);
//        user.getAddresses().add(address);
//        userRepository.save(user);
//        LOGGER.info("\nSaved address: {}", address);
    }
}
