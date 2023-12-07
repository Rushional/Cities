package com.rushional.cities.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitiesResponse implements Serializable {

    private List<CityDto> cities;
    @JsonProperty("per_page")
    private int perPage;
    private int page;
    @JsonProperty("total_pages")
    private int totalPages;
    @JsonProperty("total_cities")
    private long totalCities;
}
