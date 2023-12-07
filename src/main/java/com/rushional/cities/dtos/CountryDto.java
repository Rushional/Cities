package com.rushional.cities.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDto implements Serializable {

    private  Long id;
    private String name;
    @JsonProperty("flag_path")
    private String flagPath;
}
