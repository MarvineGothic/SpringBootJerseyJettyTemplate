package org.example.infrastructure.datasource.repository;

import lombok.RequiredArgsConstructor;
import org.example.domain.entity.Address;
import org.example.domain.repository.AddressRepository;
import org.example.error.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@RequiredArgsConstructor
@Transactional
@Service
public class AddressRepositoryImpl implements AddressRepository {
    private final AddressJpaRepository addressJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public Address save(String handle, Address address) {
        var user = userJpaRepository.findByHandle(handle)
                .orElseThrow(() -> new ServiceException("User not found", HttpStatus.NOT_FOUND.value()));
        var addresses = new ArrayList<>(user.getAddresses());
        addresses.add(AddressRepositoryMapper.fromDomain(address));
        user.setAddresses(addresses);
        return address;
    }
}
