package com.rushional.cities.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountriesResponse {

    @Serial
    private static final long serialVersionUID = 3026313130574697297L;

    private List<CountryDto> countries;
    @JsonProperty("per_page")
    private int perPage;
    private int page;
    @JsonProperty("total_pages")
    private int totalPages;
    @JsonProperty("total_cities")
    private long totalCities;
}
