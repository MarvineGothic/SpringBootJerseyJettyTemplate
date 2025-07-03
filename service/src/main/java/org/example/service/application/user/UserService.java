package org.example.service.application.user;

import org.example.error.ServiceException;
import org.example.model.request.UserRequestModel;
import org.example.model.response.AddressResponseModel;
import org.example.model.response.UserResponseModel;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<UserResponseModel> getUsers();
    UserResponseModel getUser(Long id);
    UserResponseModel createUser(UserRequestModel userRequestModel) throws ServiceException;
    Set<AddressResponseModel> getUserAddresses(Long id);
}
