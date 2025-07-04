package org.example.application.service.address;

import lombok.RequiredArgsConstructor;
import org.example.domain.repository.AddressRepository;
import org.example.model.request.AddressRequestModel;
import org.example.model.response.AddressResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final AddressRepository addressRepository;

    @Override
    public AddressResponseModel addAddress(AddressRequestModel address) {
//        addressRepository.save(address)
        return null;
    }

    @Override
    public AddressResponseModel getAddress(long id) {
        return null;
    }
}
