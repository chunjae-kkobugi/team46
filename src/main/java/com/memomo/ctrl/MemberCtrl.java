package com.memomo.ctrl;

import com.memomo.dto.MemberFormDTO;
import com.memomo.entity.Member;
import com.memomo.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberCtrl {

    private final MemberService memberService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginMember() {
        return "member/login";
    }

    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "member/login";
    }

    @GetMapping("/loginPro")
    public String loginPro() {
        return "redirect:/";
    }

    @GetMapping("/join")
    public String join(Model model) {
        MemberFormDTO member = new MemberFormDTO();
        model.addAttribute("member", member);
        return "member/join";
    }

    @PostMapping("/joinPro")
    public String joinPro(@Valid MemberFormDTO memberFormDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "member/join";
        }
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/join";
        }
        return "redirect:/";
    }

    @PostMapping("/idCheckPro")
    public ResponseEntity<Boolean> idCheck(@RequestBody MemberFormDTO member) throws Exception {
        String id = member.getId();
        boolean result = memberService.idCheck(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/mypage")
    public String myPage(Model model) {
        //String id = principal.getName();
        String id = memberService.getLoginId();
        System.out.println("회원 정보 : " + memberService.memberDetail(id));

        //MemberDTO member = new MemberDTO();
        //System.out.println("아이디 : " + id);
        //System.out.println("회원 정보 : " + memberService.memberDetail(id));
        //String id = (String) session.getAttribute("sid");
        //MemberDTO dto = modelMapper.map(memberService.memberDetail(id), MemberDTO.class);
        //model.addAttribute("member", memberService.memberDetail(id));
        //System.out.println("멤버디티오 : " + dto);
        //model.addAttribute("member", dto);
        return "member/mypage";
    }

}
