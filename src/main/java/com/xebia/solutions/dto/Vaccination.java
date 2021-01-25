package com.xebia.solutions.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vaccination {

	private String user;

	private String centre;

	private String vaccine;

}