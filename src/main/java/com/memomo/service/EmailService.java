package com.memomo.service;

import jakarta.mail.internet.MimeMessage;

public interface EmailService {
    MimeMessage createMail(String mail);
    int sendMail(String mail);
}
