package com.shoply.Products.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String to, String token){
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(to);
            mailMessage.setFrom("adedipupoadeagbo@gmail.com");
            mailMessage.setSubject("Click on the link to upgrade your account");

            String messageLink= """
                   
                        Click on the link to upgrade your account
                  
                        http://localhost:8080/validateToken?token=%s
                    """.formatted(token);

            mailMessage.setText(messageLink);
            mailSender.send(mailMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
