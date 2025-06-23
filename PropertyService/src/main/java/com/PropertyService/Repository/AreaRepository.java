package com.PropertyService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.PropertyService.Entity.Area;

public interface AreaRepository extends JpaRepository<Area, Long> {

	Area findByAreaName(String area);

}
