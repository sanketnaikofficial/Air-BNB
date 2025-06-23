package com.PropertyService.Repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.PropertyService.Entity.State;

public interface StateRepository extends JpaRepository<State, Long> {

	
	State findByStateName(String stateName);
	
}
