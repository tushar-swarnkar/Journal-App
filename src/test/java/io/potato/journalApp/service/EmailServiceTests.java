package io.potato.journalApp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {

    @Autowired
    private EmailService emailService;

    @Test
    public void testSendMail() {
        emailService.sendEmail(
                "abhityadav111@gmail.com",
                "Testing java mail sender",
                "Kya haal hai bhai ke! Placement ka kya chal rha? ");
    }

}
