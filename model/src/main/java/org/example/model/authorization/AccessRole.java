package org.example.model.authorization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import org.example.model.error.ServiceException;

public enum AccessRole {
    ROLE_USER,
    ROLE_ADMIN;

//    private final String value;
//
//    AccessRole(String value) {
//        this.value = value;
//    }
//
//    @JsonValue // Ensures the string representation is used when serializing
//    public String getValue() {
//        return value;
//    }
//
//    @JsonCreator // Allows Jackson to convert String to Enum
//    public static AccessRole fromString(String value) throws ServiceException {
//        for (AccessRole role : AccessRole.values()) {
//            if (role.value.equalsIgnoreCase(value)) {
//                return role;
//            }
//        }
//        throw new ServiceException("Invalid access role: " + value, 400);
//    }
}
