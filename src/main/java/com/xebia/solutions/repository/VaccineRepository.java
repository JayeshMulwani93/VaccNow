package com.xebia.solutions.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.xebia.solutions.model.Vaccine;

@Repository
public interface VaccineRepository extends CrudRepository<Vaccine, Integer> {

	Page<Vaccine> findByCentresId(int centreId, Pageable pageable);
}
