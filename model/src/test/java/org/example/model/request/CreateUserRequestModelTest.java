package org.example.model.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
//import org.junit.Test;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CreateUserRequestModelTest {

    @Test
    public void unmarshallingUnknownPropertiesThrowsUnrecognizedPropertyException() throws IOException {
        String jsonAsString =
                "{\"stringValue\":\"a\"," +
                        "\"intValue\":1," +
                        "\"booleanValue\":true," +
                        "\"unrecognizedStringValue\":\"something\"}";
        ObjectMapper mapper = new ObjectMapper();

        TestDto readValue = mapper.readValue(jsonAsString, TestDto.class);

        assertNotNull(readValue);
    }

    @Test
    public void unmarshallingUnknownPropertiesSuccess() throws IOException {
        String jsonAsString =
                "{\"firstname\":\"John\"," +
                        "\"lastname\":\"Doe\"," +
                        "\"password\":\"fsd87dfsdf46d5\"," +
                        "\"unrecognizedStringValue\":\"something\"}";
        ObjectMapper mapper = new ObjectMapper();

        CreateUserRequestModel readValue = mapper.readValue(jsonAsString, CreateUserRequestModel.class);

        assertNotNull(readValue);
    }
}

record TestDto(
        String stringValue,
        int intValue,
        boolean booleanValue,
        String stringValue2) {
}
