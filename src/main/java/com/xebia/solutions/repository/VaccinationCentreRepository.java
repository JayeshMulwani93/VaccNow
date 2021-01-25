package com.xebia.solutions.repository;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.xebia.solutions.model.VaccinationCentre;

@Repository
public interface VaccinationCentreRepository extends PagingAndSortingRepository<VaccinationCentre, Integer> {

}