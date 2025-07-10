package org.example.application.service.user;

import org.example.error.ServiceException;
import org.example.model.request.CreateUserRequestModel;
import org.example.model.request.UserLoginRequestModel;
import org.example.model.response.AddressResponseModel;
import org.example.model.response.UserResponseModel;
import org.example.model.response.UserSessionResponseModel;

import java.util.List;
import java.util.Set;

// Use Case Input Port
public interface UserService {
    UserSessionResponseModel login(UserLoginRequestModel userLoginRequestModel);
    @Deprecated
    UserSessionResponseModel loginBasic(UserLoginRequestModel userLoginRequestModel);
    List<UserResponseModel> getUsers();
    UserResponseModel getUser(String handle);
    UserResponseModel createUser(CreateUserRequestModel createUserRequestModel) throws ServiceException;
    Set<AddressResponseModel> getUserAddresses(String handle);
}
