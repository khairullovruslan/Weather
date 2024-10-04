package org.tomato.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationDTO {
    @NotBlank
    private String name;

    @DecimalMax(value = "90.0")
    @DecimalMin(value = "-90.0")
    @NotNull
    private double lat;

    @DecimalMax(value = "180.0")
    @DecimalMin(value = "-180.0")
    @NotNull
    private double lon;

    private String country;
}