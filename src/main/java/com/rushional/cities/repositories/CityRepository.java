package com.rushional.cities.repositories;

import com.rushional.cities.models.CityEntity;
import com.rushional.cities.models.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Long> {
  Optional<CityEntity> findByCountry(CountryEntity customer);
}
