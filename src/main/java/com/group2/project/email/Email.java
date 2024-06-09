package com.group2.project.email;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class Email implements EmailService{
    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    public SimpleMailMessage template;

    /*
    String text = String.format(template.getText(), templateArgs);
    sendSimpleMessage(to, subject, text);
     */

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@baeldung.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }



}

