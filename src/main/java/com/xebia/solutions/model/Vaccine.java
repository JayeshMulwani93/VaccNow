package com.xebia.solutions.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Vaccines")
public class Vaccine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	private String manufacturer;

	@ManyToMany(mappedBy = "vaccines")
	private List<VaccinationCentre> centres;

	public Vaccine(String name, String manufacturer) {
		super();
		this.name = name;
		this.manufacturer = manufacturer;
	}
	
	public Vaccine(int id, String name, String manufacturer) {
		super();
		this.id = id;
		this.name = name;
		this.manufacturer = manufacturer;
	}

}