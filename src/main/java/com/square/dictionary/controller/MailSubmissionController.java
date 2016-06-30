package com.square.dictionary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
class MailSubmissionController {

	@Autowired
    private final JavaMailSender javaMailSender;

    MailSubmissionController(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @RequestMapping("/mail")
    @ResponseStatus(HttpStatus.CREATED)
    SimpleMailMessage send() {        
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("khan.square@gmail.com");
        //mailMessage.setReplyTo("someone@localhost");
        mailMessage.setFrom("mohsin.khan@teramatrix.in");
        mailMessage.setSubject("Lorem ipsum");
        mailMessage.setText("Lorem ipsum dolor sit amet [...]");
        javaMailSender.send(mailMessage);
        return mailMessage;
    }
}