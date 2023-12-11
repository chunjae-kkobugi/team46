package com.memomo.ctrl;

import com.memomo.dto.PostDTO;
import com.memomo.entity.Layout;
import com.memomo.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
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
    @Autowired
    private ModelMapper mappper;

    private static LinkedList<Long>  plist = new LinkedList<>();

    @RequestMapping("enter")
    public String postEnter(HttpServletRequest request, Model model){
        Integer bno = Integer.valueOf(request.getParameter("bno"));
        List<PostDTO> postList = postService.postList(bno);
        LinkedList<Long> plist2 = new LinkedList<>();
        for(PostDTO p:postList){
            plist2.add(p.getPno());
        }

        plist = plist2;

        model.addAttribute("postList", postList);
        return "post/postHome";
    }

    @MessageMapping("/add/{bno}")
    // Ant Path Pattern 과 template { 변수 } 가 사용가능하다. 이 template 변수는 @DestinationVariable 을 참조
    @SendTo("/stomp-receive/{bno}")
    public PostDTO postAdd(@DestinationVariable Integer bno, PostDTO postDTO /*, Principal principal*/){
        /*String sid = principal.getName();*/
        // Long pno = postService.postAdd(postDTO);

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

    @MessageMapping("/sort/{bno}")
    @SendTo("/stomp-receive/{bno}")
    public void postMove(@DestinationVariable Integer bno, Layout layout){
        int originIdx = plist.indexOf(layout.getPno()); // 나의 기존 정렬 순서
        // 나의 기존 이전 노드에 나의 기존 다음 노드의 값을 넣어야 함
        Long oldBefore = ((originIdx-1)>=0)?plist.get(originIdx-1): 0; // 나의 기존 이전 노드. 내가 head 인 경우 0
        Long oldNext = ((originIdx+1)<plist.size()) ? plist.get(originIdx+1): 0; // 나의 기존 다음 노드. 내가 tail 인 경우 0

        System.out.println("WHAT IS gPriority");
        System.out.println(layout);
        int changedIdx = layout.getGPriority(); // 나의 새로운 정렬 순서

        // 나의 새로운 이전 노드가 가졌던 다음 노드의 값은 내가 가지고, 이전 노드에는 나를 집어넣어야 함
        Long newNext = (changedIdx!=plist.size()) ? plist.get(changedIdx) : 0; // 나의 다음 노드. 내가 tail 이 되는 경우 0
        Long newBefore = ((changedIdx-1)>=0)?plist.get(changedIdx-1) : 0; // 나의 새로운 이전 노드 내가 head 가 되는 경우 0
        plist.remove(originIdx);
        plist.add(changedIdx, layout.getPno());

        System.out.println(oldBefore+" "+oldNext+" "+newBefore+" "+newNext);

        postService.postSort(oldBefore, oldNext, newBefore, newNext, layout.getPno(), bno);

        System.out.println(layout);
    }
}
