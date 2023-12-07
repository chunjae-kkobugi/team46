package com.memomo.ctrl;

import com.memomo.dto.PostDTO;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/post")
public class PostCtrl {
    @RequestMapping("enter")
    public String postEnter(){
        return "post/postHome";
    }

    @MessageMapping("/add/{bno}")
    // Ant Path Pattern 과 template { 변수 } 가 사용가능하다. 이 template 변수는 @DestinationVariable 을 참조
    @SendTo("/stomp-receive/{bno}")
    public void postAdd(@DestinationVariable Integer bno, PostDTO postDTO /*, Principal principal*/){
        /*String sid = principal.getName();*/
        System.out.println("THIS IS WHAT I GOT");
        System.out.println(postDTO);
    }

    @MessageMapping("/edit/{bno}")
    @SendTo("/stomp-receive/{bno}")
    public void postEdit(@DestinationVariable Integer bno, PostDTO postDTO){
        System.out.println(postDTO);
    }

    @MessageMapping("/remove/{bno}")
    @SendTo("/stomp-receive/{bno}")
    public void postRemove(@DestinationVariable Integer bno, PostDTO postDTO){
        System.out.println(postDTO);
    }

    @MessageMapping("/move/{bno}")
    @SendTo("/stomp-receive/{bno}")
    public void postMove(@DestinationVariable Integer bno, PostDTO postDTO){
        System.out.println(postDTO);
    }
}
