package org.tomato.weather.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.tomato.weather.validator.annotation.ValidLogin;
import org.tomato.weather.validator.annotation.ValidPassword;

@Builder
@Data
public class UserRegistrationDto {

    @ValidLogin
    @NotNull
    private String login;

    @ValidPassword
    @NotNull
    private String password;
}
