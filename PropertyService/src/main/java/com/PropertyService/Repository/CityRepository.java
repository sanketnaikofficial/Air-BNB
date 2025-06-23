package com.PropertyService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.PropertyService.Entity.City;

public interface CityRepository extends JpaRepository<City, Long> {

	City findByCityName(String city);

}
