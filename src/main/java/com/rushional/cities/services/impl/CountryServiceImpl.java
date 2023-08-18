package com.rushional.cities.services.impl;

import com.rushional.cities.dtos.CountryDto;
import com.rushional.cities.mappers.CitiesMapper;
import com.rushional.cities.repositories.CountryRepository;
import com.rushional.cities.services.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;
    private final CitiesMapper mapper;

    @Override
    public List<CountryDto> getAll() {
        return countryRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
