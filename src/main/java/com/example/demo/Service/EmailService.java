package com.example.demo.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    JavaMailSender javaMailSender;


    public void sendEmail(String subject, int message, String to) {

        SimpleMailMessage message1=new SimpleMailMessage();

        ///////sender mail here
        message1.setFrom("xyz@gmail.com");

        message1.setTo(to);
        message1.setText(String.valueOf(message));
        message1.setSubject(subject);

        javaMailSender.send(message1);

        System.out.println("mail send");

    }
}