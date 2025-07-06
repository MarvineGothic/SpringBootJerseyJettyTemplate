package org.example.application.service.user;

import org.example.error.ServiceException;
import org.example.model.request.UserRequestModel;
import org.example.model.response.AddressResponseModel;
import org.example.model.response.UserResponseModel;

import java.util.List;
import java.util.Set;

// Use Case Input Port
public interface UserService {
    List<UserResponseModel> getUsers();
    UserResponseModel getUser(long id);
    UserResponseModel createUser(UserRequestModel userRequestModel) throws ServiceException;
    Set<AddressResponseModel> getUserAddresses(long id);
}
