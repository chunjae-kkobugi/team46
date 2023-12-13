package com.memomo.ctrl;

import com.memomo.dto.PostDTO;
import com.memomo.entity.Board;
import com.memomo.entity.Layout;
import com.memomo.service.BoardService;
import com.memomo.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@Log4j2
@RequestMapping("/post")
public class PostCtrl {
    @Autowired
    private PostService postService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private ModelMapper mappper;

    private static LinkedList<Long>  plist = new LinkedList<>();

    @RequestMapping("detail")
    public String postEnter(HttpServletRequest request, Model model){
        Integer bno = Integer.valueOf(request.getParameter("bno"));
        List<PostDTO> postList = postService.postList(bno);
        LinkedList<Long> plist2 = new LinkedList<>();
        for(PostDTO p:postList){
            plist2.add(p.getPno());
        }

        plist = plist2;

        Board board = boardService.boardDetail(bno);
        model.addAttribute("detail", board);
        model.addAttribute("postList", postList);
        return "board/boardDetail";
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
    @SendTo("/stomp-receive/sort/{bno}")
    public LinkedList<Long> postMove(@DestinationVariable Integer bno, Layout layout){
        int originIdx = plist.indexOf(layout.getPno()); // 나의 기존 정렬 순서
        // 나의 기존 이전 노드에 나의 기존 다음 노드의 값을 넣어야 함
        Long oldBefore = ((originIdx)>0)?plist.get(originIdx-1): 0; // 나의 기존 이전 노드. 내가 head 인 경우 0
        Long oldNext = ((originIdx+1)<plist.size()) ? plist.get(originIdx+1): 0; // 나의 기존 다음 노드. 내가 tail 인 경우 0

        int changedIdx = layout.getGPriority(); // 나의 새로운 정렬 순서

        plist.remove(originIdx);
        plist.add(changedIdx, layout.getPno());
        // 나의 새로운 이전 노드가 가졌던 다음 노드의 값은 내가 가지고, 이전 노드에는 나를 집어넣어야 함
        Long newBefore = ((changedIdx)>0)? plist.get(changedIdx-1) : 0; // 나의 새로운 이전 노드 내가 head 가 되는 경우 0
        Long newNext = (changedIdx<(plist.size()-1)) ? plist.get(changedIdx+1) : 0; // 나의 새로운 다음 노드. 내가 tail 이 되는 경우 0

        postService.postSort(oldBefore, oldNext, newBefore, newNext, layout.getPno(), bno);
        return plist;
    }

    @MessageMapping("/add/{bno}")
    // Ant Path Pattern 과 template { 변수 } 가 사용가능하다. 이 template 변수는 @DestinationVariable 을 참조
    @SendTo("/stomp-receive/{bno}")
    public PostDTO postRegister(@RequestParam("bno") Integer bno, PostDTO dto, MultipartFile postFile, HttpServletRequest request, BindingResult bindingResult) {
        HttpSession session = request.getSession();
        log.info("post register start------------------------------");

        if (bindingResult.hasErrors()) {
            log.info("has error-------------------------------------------");
            return null;
        }
        log.info(dto);
        String sid = (String) session.getAttribute("sid");
        dto.setAuthor(sid);
        dto.setBno(bno);
        dto.setPstatus("ACTIVE");
        // 로컬 경로
        String uploadDir = "D:\\kim\\project\\tproj\\project06\\team46\\src\\main\\resources\\static\\images\\postImage\\";

//        서버 경로
//            ServletContext application = request.getSession().getServletContext();
//            String uploadDir = application.getRealPath("/images/postImage");

        Long oldTail = plist.peekLast();
        Long pno = postService.postAdd(dto, postFile, uploadDir, oldTail);
        return postService.postGet(pno);
    }
}