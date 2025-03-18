package org.example.validator.enumvalidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.stream.Stream;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {
    private List<String> acceptedValues;

    @Override
    public void initialize(ValidEnum annotation) {
        ConstraintValidator.super.initialize(annotation);
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .toList();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        return acceptedValues.contains(value.toUpperCase());
    }
}
