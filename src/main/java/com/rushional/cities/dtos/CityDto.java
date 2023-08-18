package com.rushional.cities.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 3026313130574697297L;

    Long id;
    @JsonProperty("city_name")
    String cityName;
    @JsonProperty("country_name")
    String countryName;
    @JsonProperty("flag_path")
    String flagPath;
}
