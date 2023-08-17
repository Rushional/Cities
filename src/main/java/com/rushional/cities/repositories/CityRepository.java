package com.rushional.cities.repositories;

import com.rushional.cities.models.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Long> {

  List<CityEntity> findByName(String name);

  @Query("SELECT city FROM city city JOIN FETCH city.country country " +
          "WHERE country.name = ?1")
  List<CityEntity> findByCountryName(String countryName);

  @Query("SELECT city FROM city city JOIN FETCH city.country country " +
          "WHERE city.name = ?1 AND country.name = ?2")
  List<CityEntity> findByCityNameAndCountryName(String cityName, String countryName);
}
