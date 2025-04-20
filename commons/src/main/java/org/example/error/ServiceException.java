package org.example.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceException extends RuntimeException {
    private int statusCode;

    public ServiceException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
