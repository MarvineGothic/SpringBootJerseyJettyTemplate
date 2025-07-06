package org.example.application.service.address;

import lombok.RequiredArgsConstructor;
import org.example.domain.entity.AddressEntity;
import org.example.domain.repository.AddressRepository;
import org.example.model.request.AddressRequestModel;
import org.example.model.response.AddressResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl implements UserAddressService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserAddressServiceImpl.class);

    private final AddressRepository addressRepository;

    @Override
    public AddressResponseModel addAddress(long userId, AddressRequestModel addressRequestModel) {
        var address = addressRepository.save(userId, fromRequest(addressRequestModel));
        return mapAddressResponse(address);
    }

    @Override
    public AddressResponseModel getAddress(long id) {
        return null;
    }

    public static AddressEntity fromRequest(AddressRequestModel addressRequestModel) {
        return AddressEntity.builder()
                .house(addressRequestModel.getHouse())
                .floor(addressRequestModel.getFloor())
                .street(addressRequestModel.getStreet())
                .postCode(addressRequestModel.getPostCode())
                .city(addressRequestModel.getCity())
                .country(addressRequestModel.getCountry())
                .build();
    }

    public static AddressResponseModel mapAddressResponse(AddressEntity address) {
        return AddressResponseModel.builder()
                .house(address.getHouse())
                .floor(address.getFloor())
                .street(address.getStreet())
                .postCode(address.getPostCode())
                .city(address.getCity())
                .country(address.getCountry())
                .build();
    }
}
