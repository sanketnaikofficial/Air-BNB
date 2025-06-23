package com.PropertyService.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PropertyImages {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String propertyImageUrl;
	
	@ManyToOne
	@JoinColumn(name="property_id")
	private Property property;
	
	@Column(name="valid")
	private Boolean valid = true;
	@Column(name="rejection_reason")
    private String rejectionReason;

}
