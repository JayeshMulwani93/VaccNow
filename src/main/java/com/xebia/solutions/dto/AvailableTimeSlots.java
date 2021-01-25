package com.xebia.solutions.dto;

import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableTimeSlots {

	@JsonFormat(pattern="HH-mm")
	List<LocalTime> data;
}
