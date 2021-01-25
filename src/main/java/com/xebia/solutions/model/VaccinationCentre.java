package com.xebia.solutions.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "Vaccinaton_Centres")
public class VaccinationCentre {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	private long capacity;

	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(name = "VACCINE_INVENTORY",
		joinColumns = @JoinColumn(name = "centre_id"),
		inverseJoinColumns = @JoinColumn(name = "vaccine_id"))
	private List<Vaccine> vaccines;

	public Vaccine getAnyAvailableVaccine() {
		// This will return the Vaccine randomly (Assuming that there's an infinite
		// supply of vaccines)
		int index = (int) (Math.random() * vaccines.size());
		return vaccines.get(index);
	}
}