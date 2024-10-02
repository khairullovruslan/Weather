package org.tomato.weather.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.tomato.weather.validator.LoginValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = LoginValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLogin {

    String message() default "Логин не соответствует требованиям.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
