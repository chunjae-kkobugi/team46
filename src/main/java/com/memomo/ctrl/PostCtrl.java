package com.memomo.ctrl;

import com.memomo.dto.PostDTO;
import com.memomo.entity.Layout;
import com.memomo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/post")
public class PostCtrl {
    @Autowired
    private PostService postService;

    private static final LinkedList<Long>  plist = new LinkedList<>();

    @RequestMapping("enter")
    public String postEnter(Model model){
        List<PostDTO> postList = new ArrayList<>();

        for(int i=1; i<=9; i++){
            PostDTO p = new PostDTO();
            p.setPno((long) i);
            p.setContent("item"+i);
            plist.add((long) i);
            postList.add(p);
        }

        model.addAttribute("plist", postList);
        return "post/postHome";
    }

    @MessageMapping("/add/{bno}")
    // Ant Path Pattern 과 template { 변수 } 가 사용가능하다. 이 template 변수는 @DestinationVariable 을 참조
    @SendTo("/stomp-receive/{bno}")
    public PostDTO postAdd(@DestinationVariable Integer bno, PostDTO postDTO /*, Principal principal*/){
        /*String sid = principal.getName();*/
        // Long pno = postService.postAdd(postDTO);
        System.out.println(postDTO);
        postDTO.setAction("ADD");

        return postDTO;
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
        int originIdx = plist.indexOf(postDTO.getPno());
        int changedIdx = postDTO.getLayout().getPriority();
        plist.remove(originIdx);
        plist.add(changedIdx, postDTO.getPno());

        System.out.println(postDTO);
    }
}
