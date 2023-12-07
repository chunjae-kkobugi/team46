package com.memomo.ctrl;

import com.memomo.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class EmailCtrl {
    private final EmailService emailService;

    @ResponseBody
    @PostMapping("/send-mail")
    public String MailSend(String mail) {
        int number = emailService.sendMail(mail);
        return String.valueOf(number);
    }

}
