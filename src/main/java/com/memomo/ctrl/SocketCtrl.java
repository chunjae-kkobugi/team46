package com.memomo.ctrl;

import com.memomo.dto.PostDTO;
import com.memomo.entity.*;
import com.memomo.service.BoardGroupService;
import com.memomo.service.BoardService;
import com.memomo.service.MemberService;
import com.memomo.service.PostService;
import jakarta.servlet.http.Cookie;
import com.memomo.service.*;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import java.util.*;

@Controller
@Log4j2
@CrossOrigin(origins = "*")
public class SocketCtrl {
    @Autowired
    private PostService postService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private LikesService likesService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private BoardGroupService boardGroupService;
    @Autowired
    private ModelMapper mappper;
    @Autowired CommentService commentService;

    private static LinkedList<Long> plist = new LinkedList<>();

    @RequestMapping("/post/detail")
    public String postEnter(HttpServletRequest request, HttpServletResponse response, Model model) {
        Integer bno = Integer.valueOf(request.getParameter("bno"));
        String sid = memberService.getLoginId();
        //System.out.println("sid : " + sid);
        Cookie cookie = WebUtils.getCookie(request, "nickCookie");
        if (cookie == null && sid.equals("")) {
            return "redirect:/member/enter/" + bno;
        } else if(sid.equals("")){
            sid = cookie.getValue();
        }

        List<PostDTO> postList = postService.postListAll(bno);
        List<BoardGroup> groupList = boardGroupService.groupList(bno);
        LinkedList<Long> plist2 = new LinkedList<>();

        for (PostDTO p : postList) {
            plist2.add(p.getPno());
        }

        plist = plist2;

        Board board = boardService.boardDetail(bno);
        List<Likes> myLikes = likesService.myLikes(bno, sid);

        model.addAttribute("detail", board);
        model.addAttribute("postList", postList);
        model.addAttribute("myLikes", myLikes);
        model.addAttribute("sid", sid);
        model.addAttribute("groupList", groupList);
        return "board/boardDetail";
    }

    @MessageMapping("/remove/{bno}")
    @SendTo("/stomp-receive/remove/{bno}")
    public Long postRemove(@DestinationVariable Integer bno, PostDTO postDTO) {
        Long pno = postDTO.getPno();

        int originIdx = plist.indexOf(pno);
        // head, Right, 이전 노드 / tail, Left, 다음 노드
        Long oldLeft = ((originIdx) > 0) ? plist.get(originIdx - 1) : 0;
        // 나의 기존 다음 노드. 내가 tail 인 경우 0
        Long oldRight = ((originIdx + 1) < plist.size()) ? plist.get(originIdx + 1) : 0;
        // 나의 기존 이전 노드. 내가 head 인 경우 0

        postService.postRemove(pno, oldLeft, oldRight, bno);
        plist.remove(originIdx);

        System.out.println(postDTO);
        return postDTO.getPno();
    }

    @MessageMapping("/sort/{bno}")
    @SendTo("/stomp-receive/sort/{bno}")
    public LinkedList<Long> postMove(@DestinationVariable Integer bno, Layout layout) {
        int originIdx = plist.indexOf(layout.getPno()); // 나의 기존 정렬 순서
        // 나의 기존 이전 노드에 나의 기존 다음 노드의 값을 넣어야 함

        // head, Right, 이전 노드 / tail, Left, 다음 노드
        Long oldLeft = ((originIdx) > 0) ? plist.get(originIdx - 1) : 0;
        // 나의 기존 다음 노드. 내가 tail 인 경우 0
        Long oldRight = ((originIdx + 1) < plist.size()) ? plist.get(originIdx + 1) : 0;
        // 나의 기존 이전 노드. 내가 head 인 경우 0

        int changedIdx = layout.getGPriority(); // 나의 새로운 정렬 순서
        plist.remove(originIdx);
        plist.add(changedIdx, layout.getPno());

        // 나의 새로운 이전 노드가 가졌던 다음 노드의 값은 내가 가지고, 이전 노드에는 나를 집어넣어야 함
        Long newLeft = ((changedIdx) > 0) ? plist.get(changedIdx - 1) : 0;
        // 나의 새로운 다음 노드. 내가 tail 이 되는 경우 0
        Long newRight = (changedIdx < (plist.size() - 1)) ? plist.get(changedIdx + 1) : 0;
        // 나의 새로운 이전 노드 내가 head 가 되는 경우 0

        postService.postSort(oldRight, oldLeft, newRight, newLeft, layout.getPno(), bno);
        return plist;
    }

    @MessageMapping("/add/{bno}")
    // Ant Path Pattern 과 template { 변수 } 가 사용가능하다. 이 template 변수는 @DestinationVariable 을 참조
    @SendTo("/stomp-receive/add/{bno}")
    public PostDTO postAdd(@DestinationVariable Integer bno, PostDTO dto) {
        Long pno = dto.getPno();
        log.info(pno + "------pno");
        plist.add(pno);
        return postService.postGet(pno);
    }

    ;

    @PostMapping("/post/add")
    @ResponseBody
    public Long postAddPro(@ModelAttribute PostDTO dto, @RequestParam("postFile") Optional<MultipartFile> postFile, BindingResult bindingResult, HttpServletRequest request) {
        log.info("post register start------------------------------");

        if (bindingResult.hasErrors()) {
            log.info("has error-------------------------------------------");
            return null;
        }
        log.info(dto);
        Cookie cookie = WebUtils.getCookie(request, "nickCookie");
        String loginId = memberService.getLoginId();
        String sid = loginId.equals("") ? cookie.getValue() : loginId;
        log.info("---------- sid: " + sid);
        dto.setAuthor(sid);
        dto.setPstatus("ACTIVE");
        // 로컬 경로
//        String uploadDir = "C:\\Users\\User\\Desktop\\uploadImg\\";

//        서버 경로
        ServletContext application = request.getSession().getServletContext();
        String uploadDir = application.getRealPath("/images/postImage");

        Long pno;
        if (!postFile.isPresent() || postFile.isEmpty()) {
            pno = postService.postAdd(dto, null, uploadDir);
        } else {
            pno = postService.postAdd(dto, postFile.orElseThrow(), uploadDir);
        }

        return pno;
    }

    @MessageMapping("/edit/{bno}")
    // Ant Path Pattern 과 template { 변수 } 가 사용가능하다. 이 template 변수는 @DestinationVariable 을 참조
    @SendTo("/stomp-receive/edit/{bno}")
    public PostDTO postEdit(@DestinationVariable Integer bno, PostDTO dto) {
        Long pno = dto.getPno();
        log.info(pno + "------pno");
        return postService.postGet(pno);
    }

    ;

    @PostMapping("/post/edit")
    @ResponseBody
    public Long postEditPro(@ModelAttribute PostDTO dto, @RequestParam("postFile") Optional<MultipartFile> postFile, BindingResult bindingResult, HttpServletRequest request) {
        log.info("post EDIT start------------------------------");

        if (bindingResult.hasErrors()) {
            log.info("has error-------------------------------------------");
            return null;
        }
        log.info(dto);
        // 로컬 경로
        String uploadDir = "C:\\Users\\1889018\\Desktop\\uploadImg\\";

//        서버 경로
//            ServletContext application = request.getSession().getServletContext();
//            String uploadDir = application.getRealPath("/images/postImage");

        Long pno;
        if (!postFile.isPresent() || postFile.isEmpty()) {
            pno = postService.postEdit(dto, null, uploadDir);
        } else {
            pno = postService.postEdit(dto, postFile.orElseThrow(), uploadDir);
        }

        return pno;
    }

    @MessageMapping("/likes/{bno}")
    @SendTo("/stomp-receive/likes/{bno}")
    public PostDTO postLikes(@DestinationVariable Integer bno, PostDTO dto) {
        int cnt = likesService.toggleLikes(bno, dto.getPno(), dto.getAuthor()); // 바뀐 좋아요 수
        dto.setLikes((long) cnt);
        return dto;
    }

    @GetMapping("/post/getPost/{pno}")
    @ResponseBody
    public PostDTO getPost(@PathVariable Long pno, HttpServletRequest request) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> pno : " + pno);
        return postService.getPost(pno);
    }

    @PostMapping("/post/addComment")
    @ResponseBody
    public Comment commentAdd(@RequestParam("pno") Long pno, @RequestParam("content") String content, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        log.info("comment register>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        Cookie cookie = WebUtils.getCookie(request, "nickCookie");
        String loginId = memberService.getLoginId();
        String sid = loginId.equals("") ? cookie.getValue() : loginId;
        Comment comment = new Comment();
        log.info("---------- sid: " + sid);
        comment.setAuthor(sid);
        comment.setPno(pno);
        comment.setContent(content);
        return commentService.commentAdd(comment);
    }

    @MessageMapping("/groupAdd/{bno}")
    @SendTo("/stomp-receive/groupAdd/{bno}")
    public BoardGroup groupAdd(@DestinationVariable Integer bno, BoardGroup group){
        group.setBno(bno);
        return boardGroupService.groupAdd(group);
    };

    @MessageMapping("/groupEdit/{bno}")
    @SendTo("/stomp-receive/groupEdit/{bno}")
    public BoardGroup groupEdit(@DestinationVariable Integer bno, BoardGroup group){
        return boardGroupService.groupEdit(group);
    };

    @MessageMapping("/groupRemove/{bno}")
    @SendTo("/stomp-receive/groupRemove/{bno}")
    public Integer groupRemove(@DestinationVariable Integer bno, BoardGroup group){
        boardGroupService.groupRemove(group.getGno());
        return group.getGno();
    }

    @MessageMapping("/comments/{pno}")
    @SendTo("/stomp-receive/comments/{pno}")
    public Long commentCount(@DestinationVariable Long pno){
        return commentService.commentCount(pno);
    }
}