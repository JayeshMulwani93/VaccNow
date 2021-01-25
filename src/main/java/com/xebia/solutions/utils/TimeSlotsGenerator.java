package com.xebia.solutions.utils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeSlotsGenerator {

	@Bean
	public List<LocalTime> getAllTimeSlots() {
		List<LocalTime> timeSlots = new ArrayList<>(96);

		for (int hour = 0; hour < 24; hour++) {
			for (int minute = 0; minute < 60; minute = minute + 15) {
				timeSlots.add(LocalTime.of(hour, minute));
			}
		}
		return timeSlots;
	}
}
