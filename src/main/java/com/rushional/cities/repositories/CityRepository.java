package com.rushional.cities.repositories;

import com.rushional.cities.models.CityEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Long> {

  Page<CityEntity> findByName(String name, Pageable pageable);

  @Query("SELECT city FROM city city JOIN FETCH city.country country " +
          "WHERE country.name = ?1")
  Page<CityEntity> findByCountryName(String countryName, Pageable pageable);

  @Query("SELECT city FROM city city JOIN FETCH city.country country " +
          "WHERE city.name = ?1 AND country.name = ?2")
  Page<CityEntity> findByCityNameAndCountryName(String cityName, String countryName, Pageable pageable);
}
