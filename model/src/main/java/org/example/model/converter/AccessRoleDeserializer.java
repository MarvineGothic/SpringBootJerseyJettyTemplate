package org.example.model.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.example.model.authorization.AccessRole;
import org.example.model.error.ServiceException;

import java.io.IOException;

public class AccessRoleDeserializer extends JsonDeserializer<AccessRole> {
    @Override
    public AccessRole deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        String value = jsonParser.getText().toUpperCase();
        try {
            return AccessRole.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new ServiceException("Invalid access role: " + value, 400);
        }
    }
}
