package model.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.error.ResponseError;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

@SpringBootTest
public class ResponseErrorDtoTest {

    @Test
    public void ignoreNullFields() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ResponseError responseError = new ResponseError();
        responseError.setCode(404);
        responseError.setError("Not found");

        String dtoAsString = mapper.writeValueAsString(responseError);

        assertThat(dtoAsString, containsString("code"));
        assertThat(dtoAsString, containsString("error"));
        assertThat(dtoAsString, not(containsString("httpReason")));
        assertThat(dtoAsString, not(containsString("httpStatus")));
        assertThat(dtoAsString, not(containsString("path")));
        assertThat(dtoAsString, not(containsString("timestamp")));
        assertThat(dtoAsString, not(containsString("requestId")));
        assertThat(dtoAsString, not(containsString("transactionError")));
    }
}
