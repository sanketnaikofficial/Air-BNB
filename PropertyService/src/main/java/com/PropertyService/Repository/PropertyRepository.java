package com.PropertyService.Repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.PropertyService.Entity.Property;

public interface PropertyRepository extends JpaRepository<Property, Long> {

	boolean existsByPropertyName(String name);
	@Query("""
		    SELECT DISTINCT p 
		    FROM Property p
		    JOIN p.rooms r
		    JOIN RoomAvailability ra ON ra.room = r
		    WHERE (
		        LOWER(p.city.cityName) LIKE LOWER(CONCAT('%', :name, '%')) OR
		        LOWER(p.area.areaName) LIKE LOWER(CONCAT('%', :name, '%')) OR
		        LOWER(p.state.stateName) LIKE LOWER(CONCAT('%', :name, '%'))
		    )
		    AND ra.availableDate = :date
		""")
		List<Property> searchProperty(@Param("name") String name, @Param("date") LocalDate date); // Use LocalDate here

	


}
