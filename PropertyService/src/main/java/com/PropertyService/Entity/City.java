package com.PropertyService.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class City {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private String cityName;


}
