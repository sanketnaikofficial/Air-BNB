package com.PropertyService.Entity;

import java.time.LocalDate ;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RoomAvailability {
	 	@Id 
	 	@GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    
	    @JsonFormat(pattern = "yyyy-MM-dd")
	 	private LocalDate availableDate;
	    private Integer availableCount;
	    @ManyToOne
	    @JoinColumn(name = "rooms_id")
	    @JsonIgnore
	    private Rooms room;
}
