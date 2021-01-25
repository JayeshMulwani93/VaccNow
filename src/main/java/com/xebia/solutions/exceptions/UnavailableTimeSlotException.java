package com.xebia.solutions.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Unavailable timeslots!")
public class UnavailableTimeSlotException extends RuntimeException {

	private static final long serialVersionUID = 1L;

}