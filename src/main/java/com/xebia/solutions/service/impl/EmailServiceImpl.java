package com.xebia.solutions.service.impl;

import org.springframework.stereotype.Service;

import com.xebia.solutions.service.EmailService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

	public static final String EMAIL_FORMAT_LOG = "Sent email to %s with content %s";

	@Override
	public void sendEmail(String emailId, String content) {
		log.debug(String.format(EMAIL_FORMAT_LOG, emailId, content));
	}

}
