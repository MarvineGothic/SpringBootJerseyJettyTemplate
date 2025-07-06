package org.example.application.service.address;

import org.example.model.request.AddressRequestModel;
import org.example.model.response.AddressResponseModel;

public interface UserAddressService {
    AddressResponseModel addAddress(long userId, AddressRequestModel address);
    AddressResponseModel getAddress(long id);
}
