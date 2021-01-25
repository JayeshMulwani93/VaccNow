package com.xebia.solutions.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.xebia.solutions.model.User;
import com.xebia.solutions.service.impl.EmailServiceImpl;

@ExtendWith(SpringExtension.class)
public class EmailServiceTest {

	@Spy
	private EmailService service = new EmailServiceImpl();

	private User user = new User("A", "A@gmail.com");

	@Test
	public void testSendEmail() {
		service.sendEmail(user.getEmailId(), "Test Email!");
	}

}