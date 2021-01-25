package com.xebia.solutions.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@AllArgsConstructor
@Data
public class AppointmentId implements Serializable {

	private static final long serialVersionUID = 1L;

	public AppointmentId() {
		
	}

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "slot_id")
	private Long slotId;

}