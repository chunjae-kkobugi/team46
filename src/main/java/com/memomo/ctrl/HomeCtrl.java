package com.memomo.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeCtrl {
    @RequestMapping("")
    public String home(){
        return "/index";
    }

    @RequestMapping("/team46")
    public String home3(){
        return "/index";
    }
}