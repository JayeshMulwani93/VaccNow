package com.xebia.solutions.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Invalid user provided")
public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

}
