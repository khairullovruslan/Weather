package org.tomato.weather.dto;

import lombok.Builder;
import lombok.Data;
import org.tomato.weather.validator.annotation.ValidPassword;

@Builder
@Data
public class UserRegistrationDto {

    private String login;

    @ValidPassword
    private String password;
}
