package org.tomato.weather.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.tomato.weather.service.LoginService;
import org.tomato.weather.validator.annotation.ValidLogin;

public class LoginValidator implements ConstraintValidator<ValidLogin, String> {
    private final LoginService loginService = LoginService.getInstance();

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        if (loginService.findUserByLogin(login).isPresent()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Пользватель с таким логином уже зарегистрирован")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }


}