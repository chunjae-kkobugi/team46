package com.memomo.ctrl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;

import java.util.UUID;

@Controller
public class HomeCtrl {
    @RequestMapping("")
    public String home(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request, "nickCookie");
        System.out.println("cookie : " + cookie);
        if (cookie != null) {
            String ckValue = cookie.getValue();
            System.out.println("쿠키 : " + cookie);
            System.out.println("쿠키값 : " + ckValue);
        }
        return "/index";
    }
}
