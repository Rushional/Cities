package com.rushional.cities.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UniqueCityNamesResponse {
    private List<String> cityNames;
}
