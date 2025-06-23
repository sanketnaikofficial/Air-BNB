package com.PropertyService.Entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
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
public class Property {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String propertyName;
    private Integer numberOfBeds;
    private Integer numberOfRooms;
    private Integer numberOfBathRooms;
    private Integer numberOfGuestAllowed;
    
    @ManyToOne
    @JoinColumn(name="city_id")
    private City city;
    
    @ManyToOne
    @JoinColumn(name="state_id")
    private State state;
    
    @ManyToOne
    @JoinColumn(name="area_id")
    private Area area;
    
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rooms> rooms = new ArrayList<>();
    
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PropertyImages> images = new ArrayList<>();
}
