package com.PropertyService.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.PropertyService.Entity.RoomAvailability;

public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailability, Long> {

	
	List<RoomAvailability> findByRoom_Id(Long roomId);

	
	@Query("SELECT r FROM RoomAvailability r WHERE r.room.id = :roomId AND r.availableDate = :date")
	RoomAvailability findByRoomIdAndDate(@Param("roomId") Long roomId, @Param("date") LocalDate date);

	@Query("SELECT ra FROM RoomAvailability ra WHERE ra.room.id = :roomId AND ra.availableDate = :date")
	List<RoomAvailability> findAllAvailabilityByRoomIdAndDate(@Param("roomId") Long roomId, @Param("date") LocalDate date);
}
