package com.memomo.ctrl;

import com.memomo.dto.MemberDTO;
import com.memomo.service.BoardService;
import com.memomo.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberCtrl {

    private final MemberService memberService;
    private final HttpSession session;

    @GetMapping("login")
    public String login() {
        return "member/login";
    }

    @PostMapping("loginPro")
    public String loginPro(String id, String pw, Model model, RedirectAttributes rttr) {
        //int pass = memberService.loginPro(id, pw);
        //System.out.println(id + " " + pw + " " + model);

        String result = memberService.login(id, pw);
        System.out.println("result : " + result);
        if (result.equals("Login Success")) {
            System.out.println("로그인 성공");
            session.setAttribute("sid", id);
            return "redirect:/";
        } else {
            System.out.println("로그인 실패");
            rttr.addFlashAttribute("msg", "아이디 또는 비밀번호를 잘못 입력했습니다.");
            return "redirect:/member/login";
        }
    }

    @GetMapping("join")
    public String join(Model model) {
        MemberDTO member = new MemberDTO();
        model.addAttribute("member", member);
        return "member/join";
    }

    @PostMapping("joinPro")
    public String joinPro(MemberDTO member, Model model) {
        //memberService.memberInsert(member);
        memberService.join(member);
        //model.addAttribute("msg", "가족이 되신걸 환영합니다.");
        //model.addAttribute("url", "/member/login");
        return "/member/login";
    }

    @PostMapping("idCheckPro")
    public ResponseEntity<Boolean> idCheck(@RequestBody MemberDTO member) throws Exception {
        String id = member.getId();
        boolean result = memberService.idCheck(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
