package org.tomato.weather.dto;

import lombok.Builder;
import lombok.ToString;

@Builder
public record UserDto(String login, String password) {
}
