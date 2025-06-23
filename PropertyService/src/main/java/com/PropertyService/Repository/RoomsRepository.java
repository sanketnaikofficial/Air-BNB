package com.PropertyService.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.PropertyService.Entity.Rooms;

public interface RoomsRepository extends JpaRepository<Rooms, Long> {
	@Query("SELECT r.id FROM Rooms r WHERE r.property.id = :propertyId")
	List<Long> findRoomIdsByPropertyId(@Param("propertyId") Long propertyId);
	
	@Query("SELECT r FROM Rooms r WHERE r.id = :id")
	Rooms getRoomById(@Param("id") Long id);
	
}
