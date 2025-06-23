package com.PropertyService.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Rooms {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String roomType;
	@ManyToOne
    @JoinColumn(name = "property_id")
	@JsonIgnore
    private Property property;
	
	@Column(name = "base_price")
    private Double basePrice;
	
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<RoomAvailability> availability;
    
   
   
}
