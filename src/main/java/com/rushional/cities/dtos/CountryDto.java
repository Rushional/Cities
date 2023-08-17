package com.rushional.cities.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 3026313130574697297L;

    String name;
    String flagPath;
}