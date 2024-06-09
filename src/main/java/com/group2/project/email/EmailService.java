package com.group2.project.email;


public interface EmailService {

    public void sendSimpleMessage(String to, String subject, String text);
}