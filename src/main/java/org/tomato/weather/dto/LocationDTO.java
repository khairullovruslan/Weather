package org.tomato.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationDTO that = (LocationDTO) o;
        return Double.compare(lat, that.lat) == 0 && Double.compare(lon, that.lon) == 0 && Objects.equals(name, that.name) && Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lat, lon, country);
    }

    @DecimalMax(value = "180.0")
    @DecimalMin(value = "-180.0")
    @NotNull
    private double lon;

    private String country;
    private String state;
}