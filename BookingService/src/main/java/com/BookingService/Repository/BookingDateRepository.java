package com.BookingService.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.BookingService.Entity.BookingDate;

public interface BookingDateRepository extends JpaRepository<BookingDate, Long> {
	List<BookingDate> findByBookingsId(long bookingId);

	@Query("SELECT bd.date FROM BookingDate bd WHERE bd.bookings.id = :bookingId")
    List<LocalDate> findDateByBookingId(@Param("bookingId") Long bookingId);

}
