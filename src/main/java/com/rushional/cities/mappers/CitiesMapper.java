package com.rushional.cities.mappers;

import com.rushional.cities.dtos.CountryDto;
import com.rushional.cities.models.CountryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CitiesMapper {
    CountryDto toDto(CountryEntity country);
}
