package org.example.application.service.address;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.repository.UserRepository;
import org.example.domain.valueobject.Address;
import org.example.domain.valueobject.PostCode;
import org.example.model.request.AddressRequestModel;
import org.example.model.response.AddressResponseModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAddressServiceImpl implements UserAddressService {

    private final UserRepository userRepository;

    @Override
    public AddressResponseModel addAddress(String userHandle, AddressRequestModel addressRequestModel) {
        var address = userRepository.addAddress(userHandle, fromRequest(addressRequestModel));
        return mapAddressResponse(address);
    }

    public static Address fromRequest(AddressRequestModel addressRequestModel) {
        return Address.builder()
                .house(addressRequestModel.getHouse())
                .floor(addressRequestModel.getFloor())
                .street(addressRequestModel.getStreet())
                .postCode(new PostCode(addressRequestModel.getPostCode()))
                .city(addressRequestModel.getCity())
                .country(addressRequestModel.getCountry())
                .build();
    }

    public static AddressResponseModel mapAddressResponse(Address address) {
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
