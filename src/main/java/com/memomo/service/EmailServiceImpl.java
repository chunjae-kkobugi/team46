package com.memomo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private int code;
    private String tempPassword;

    private void generateCode() {
        Random random = new Random();
        int firstDigit = 1 + random.nextInt(9); // 1부터 9까지의 숫자 생성
        StringBuilder sb = new StringBuilder();
        sb.append(firstDigit);
        for (int i = 0; i < 5; i++) {
            int randomNumber = random.nextInt(10); // 0부터 9까지의 숫자 생성
            sb.append(randomNumber);
        }
        code = Integer.parseInt(sb.toString());
    }

    private void setTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
        char[] arr = new char[10];
        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            arr[i] = charSet[idx];
        }
        tempPassword = String.valueOf(arr);
    }

    @Override
    public MimeMessage createMail(String mail) {
        generateCode();
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + code + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public MimeMessage createPwMail(String mail) {
        setTempPassword();
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("임시 비밀번호");
            String body = "";
            body += "<h3>" + "임시 비밀번호입니다." + "</h3>";
            body += "<h1>" + tempPassword + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }


    @Override
    public int sendMail(String mail) {
        MimeMessage message = createMail(mail);
        javaMailSender.send(message);
        return code;
    }

    @Override
    public String sendPw(String mail) {
        MimeMessage message = createPwMail(mail);
        javaMailSender.send(message);
        return tempPassword;
    }
}
