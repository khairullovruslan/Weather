package org.tomato.weather.exception;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import org.tomato.weather.dto.UserRegistrationDto;

import java.util.Set;

@Getter
public class RegistrationException extends RuntimeException {
    private final Set<ConstraintViolation<UserRegistrationDto>> violations;

    public RegistrationException(Set<ConstraintViolation<UserRegistrationDto>> violations) {
        this.violations = violations;
    }
}