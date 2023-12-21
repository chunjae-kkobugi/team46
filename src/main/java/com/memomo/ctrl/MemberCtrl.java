package com.memomo.ctrl;

import com.memomo.dto.*;
import com.memomo.entity.Member;
import com.memomo.repository.MemberRepository;
import com.memomo.service.BoardService;
import com.memomo.service.CustomUserDetailsService;
import com.memomo.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberCtrl {

    private final MemberService memberService;
    private final CustomUserDetailsService customUserDetailsService;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final BoardService boardService;

    @GetMapping("/login")
    public String loginMember() {
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //System.out.println("authentication : " + authentication);
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

    @GetMapping("/term")
    public String joinTerm() {
        return "member/join_term";
    }

    @GetMapping("/join")
    public String join(Model model) {
        MemberFormDTO member = new MemberFormDTO();
        //System.out.println("MemberFormDTO : " + member);
        model.addAttribute("member", member);
        return "member/join";
    }

    @PostMapping("/join")
    public String joinPro(@ModelAttribute("member") @Valid MemberFormDTO memberFormDto, BindingResult bindingResult, Model model) {
        System.out.println("bindingResult : " + bindingResult);
        if (bindingResult.hasErrors()) {
            return "member/join";
        }
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            System.out.println("e.getMessage() : " + e.getMessage());
            return "member/join";
        }
        return "redirect:/";
    }

    @PostMapping("/idCheckPro")
    public ResponseEntity<Boolean> idCheck(@RequestParam String id) throws Exception {
        boolean result = memberService.idCheck(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/mypage")
    public String myPage(Model model) {
        String id = memberService.getLoginId();
        //System.out.println("회원 정보 : " + memberService.memberDetail(id));
        Member member = memberService.memberDetail(id);
        model.addAttribute("member", member);
        return "member/mypage";
    }

    @PostMapping("/mypage")
    public String updateMember(@Valid MemberUpdateDto memberUpdateDto, Model model) {
        customUserDetailsService.updateMember(memberUpdateDto);
        return "redirect:/member/mypage";
    }

    @GetMapping("/remove")
    public String removeMember(@RequestParam String id, Model model, HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("url", "");
        if (id == null || id.isEmpty()) {
            model.addAttribute("msg", "올바른 회원 ID가 제공되지 않았습니다.");
            return "member/alert";
        }
        // 현재 로그인된 사용자의 정보 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            model.addAttribute("msg", "사용자 인증 정보를 찾을 수 없습니다.");
            return "member/alert";
        }
        String loggedInUserId = auth.getName(); // 현재 로그인된 사용자의 ID
        // 입력된 ID와 현재 로그인된 사용자의 ID 비교하여 회원 탈퇴 처리
        if (id.equals(loggedInUserId)) {
            memberService.memberRemove(id); // 회원 탈퇴 메소드
            new SecurityContextLogoutHandler().logout(request, response, auth);
            model.addAttribute("msg", "회원 탈퇴가 정상적으로 이루어졌습니다. 감사합니다.");
        } else {
            model.addAttribute("msg", "잘못된 접근입니다.");
        }
        return "member/alert";
    }

    @GetMapping("/enter/{bno}")
    public String enter(@PathVariable String bno, Model model) {
        NicknameDTO nicknameDTO = new NicknameDTO();
        Integer boardNum = Integer.valueOf(bno);
        nicknameDTO.setBno(boardNum);
        model.addAttribute("nicknameDTO", nicknameDTO);
        //System.out.println("보드 비번 : " + boardService.boardDetail(boardNum).getBpw());
        model.addAttribute("boardDetail", boardService.boardDetail(boardNum));

        return "member/enter";
    }

    @PostMapping("/nick")
    public String enterNick(NicknameDTO nicknameDTO, HttpServletResponse response, RedirectAttributes rttr, Model model) {
        Cookie nickCookie = new Cookie("nickCookie", nicknameDTO.getNickname());
        nickCookie.setPath("/");
        nickCookie.setMaxAge(60 * 60 * 24 * 1);
        response.addCookie(nickCookie);
        Integer bno = nicknameDTO.getBno();
        String bpw = boardService.boardDetail(bno).getBpw();
        if (!nicknameDTO.getPassword().equals(bpw)) {
            rttr.addFlashAttribute("msg", "비밀번호가 일치하지 않습니다.");
            return "redirect:/member/enter/" + bno;
        }
        return "redirect:/post/detail?bno=" + bno;
    }

    @GetMapping("/findId")
    public String findIdForm() {
        return "member/findId";
    }

    @PostMapping("/findId")
    public String findId(HttpServletRequest request, RedirectAttributes rttr, Model model) {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String id = memberService.findId(email, name);
        //System.out.println("찾은 아이디 : " + id);
        rttr.addFlashAttribute("rt", true);
        if (id == null) {
            return "redirect:/member/findId";
        }
        String first = id.substring(0, 3);
        String mask = "*".repeat(id.length() - 3);
        rttr.addFlashAttribute("id", first + mask);
        rttr.addFlashAttribute("name", name);
        rttr.addFlashAttribute("email", email);
        return "redirect:/member/findIdResult";
    }

    @GetMapping("/findIdResult")
    public String findIdResult() {
        return "member/findIdResult";
    }

    @GetMapping("/changePw")
    public String changePwForm(Model model) {
        MemberPwDTO memberPwDTO = new MemberPwDTO();
        model.addAttribute("memberPwDTO", memberPwDTO);
        return "member/changePw";
    }

    @PostMapping("/changePw")
    public String changePw(@Valid MemberPwDTO memberPwDTO, BindingResult bindingResult, Model model) {
        String id = memberService.getLoginId();
        Member member = memberService.memberDetail(memberService.getLoginId());

//        if (bindingResult.hasErrors()) {
//            return "member/changePw";
//        }
//        try {
//            if(!memberPwDTO.getNewPassword().equals(memberPwDTO.getNewPasswordConfirm())) {
//                model.addAttribute("errorMessage", "새로운 비밀번호와 새로운 비밀번호 확인이 일치하지 않습니다");
//            } else {
//                boolean modifyPwCheck = memberService.modifyPw(member.getMno(), memberPwDTO);
//                if(!modifyPwCheck) {
//                    model.addAttribute("errorMessage", "기존 비밀번호가 다릅니다.");
//                    return "member/myPagePw";
//                }
//            }
//        } catch (Exception e) {
//            model.addAttribute("errorMessage", e.getMessage());
//            return "member/myPagePw";
//        }

        return "redirect:/member/mypage";
    }
}
