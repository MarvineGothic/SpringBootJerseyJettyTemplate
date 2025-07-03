package org.example.model.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.assertj.core.groups.Tuple;
import org.example.model.ModelTestConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = ModelTestConfiguration.class)
class UserRequestModelValidationTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void noViolations() {
        var userRequest = UserRequestModel.builder()
                .firstName("John")
                .lastName("Doe")
                .email("jd@gmail.com")
                .password("a;lsdfjsd89f")
                .build();
        var violations = validator.validate(userRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    void allViolations() {
        var userRequest = UserRequestModel.builder()
                .firstName("")
                .lastName(null)
                .email("jd@gmail.")
                .password(" ")
                .build();
        var violations = validator.validate(userRequest);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(4);
        assertThat(violations).extracting(e -> e.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(
                        Tuple.tuple("firstName", "must not be blank"),
                        Tuple.tuple("lastName", "must not be blank"),
                        Tuple.tuple("password", "must not be blank"),
                        Tuple.tuple("email", "Invalid email")
                );
    }
}
