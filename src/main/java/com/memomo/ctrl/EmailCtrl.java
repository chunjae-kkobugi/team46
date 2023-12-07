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
    private int code;

    @ResponseBody
    @PostMapping("/send-mail")
    public String mailSend(String mail) {
        code = emailService.sendMail(mail);
        return String.valueOf(code);
    }

    @ResponseBody
    @PostMapping("/confirm")
    public boolean confirm(int inputNum) {
        return inputNum == code;
    }

}
