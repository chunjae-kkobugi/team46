package com.memomo.ctrl;

import com.memomo.dto.BoardDTO;
import com.memomo.dto.PageDTO;
import com.memomo.entity.Board;
import com.memomo.entity.BoardFile;
import com.memomo.service.BoardService;
import com.memomo.service.MemberService;
import com.memomo.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/board/")
public class BoardCtrl {
    @Autowired
    private BoardService boardService;
    @Autowired
    private PostService postService;
    @Autowired
    private MemberService memberService;

    @RequestMapping("list")
    public String boardList(Model model, HttpServletRequest request){
        PageDTO<Board, BoardDTO> pageDTO = new PageDTO<>();

        String type = request.getParameter("type");
        String keyword = request.getParameter("keyword");
        int pageNow = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
        String teacher = memberService.getLoginId();
        pageDTO.setType(type);
        pageDTO.setKeyword(keyword);
        pageDTO.setPageNow(pageNow);
        pageDTO.setTeacher(teacher);
        pageDTO.setStatus("ACTIVE");

        pageDTO = boardService.boardList(pageDTO);
        List<BoardDTO> boardList = pageDTO.getListDTO();

        for(BoardDTO board : boardList) {
            BoardFile boardFile = boardService.getBoardFile(board.getBgImage());
            board.setFile(boardFile);
        }

        if (teacher == "") {
            model.addAttribute("msg", "로그인 후 이용해 주세요");
            return "member/alert";
        }

        model.addAttribute("boardList", boardList);

        model.addAttribute("pageDTO", pageDTO);

        return "board/boardList";
    }

    // 비밀번호 있는 게시판
    @RequestMapping("bpw")
    public String boardPasswordForm(){
        return "board/password";
    }
    @PostMapping("bpw")
    public String boardPassword(){
        return null;
    }

    /*
    특정 게시판을 여는 것부터는 PostCtrl로 넘어가야 함

    @RequestMapping("detail")
    public String boardDetail(@RequestParam("bno") int bno, HttpServletRequest request, Model model) {
        BoardPostDTO boardPostDTO = boardService.boardDetail(bno);
        model.addAttribute("detail", boardPostDTO);
        return "board/boardDetail";
    }
    */

    @RequestMapping("register")
    public String boardRegisterForm() {
        return "board/boardRegister";
    }

    @PostMapping("register")
    public String boardRegister(BoardDTO boardDTO, MultipartFile boardFile, HttpServletRequest request, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException {
        //HttpSession session = request.getSession();
        log.info("board register start=====================");

        if (bindingResult.hasErrors()){
            log.info("has Error============================");
            redirectAttributes.addFlashAttribute("error", bindingResult);
            return "redirect:/board/register";
        }

        log.info(boardDTO);
        //String sid = memberService.getLoginId();
        String id = memberService.getLoginId();
        boardDTO.setTeacher(id);
//        boardDTO.setBgImage(boardDTO.getFile().getFno());
        int bno = boardService.boardAdd(boardDTO, boardFile, request);
        redirectAttributes.addFlashAttribute("result", bno);
        return "redirect:/board/list";
    }

    @PostMapping("modify")
    public String boardModify(BoardDTO boardDTO, MultipartFile boardFile, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        log.info("board modify-----------" + boardDTO);
        boardService.boardEdit(boardDTO, boardFile, request);
        redirectAttributes.addFlashAttribute("result", "modified");
        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        return "redirect:/post/detail";
    }

    @GetMapping("remove")
    public String boardRemove(@RequestParam("bno") Integer bno, RedirectAttributes redirectAttributes, HttpServletRequest request){
        log.info("board remove-------------------------------" + bno);
        boardService.boardRemove(bno);
        redirectAttributes.addFlashAttribute("result", "removed");
        redirectAttributes.addAttribute("bno", bno);
        return "redirect:/board/list";
    }
}
