package com.memomo.ctrl;

import com.memomo.dto.MemberPwDTO;
import com.memomo.dto.MemberUpdateDto;
import com.memomo.entity.Member;
import com.memomo.service.CustomUserDetailsService;
import com.memomo.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageCtrl {

    private final MemberService memberService;
    private final CustomUserDetailsService customUserDetailsService;

    @GetMapping("/info")
    public String myInfo(Model model) {
        String id = memberService.getLoginId();
        //System.out.println("회원 정보 : " + memberService.memberDetail(id));
        Member member = memberService.memberDetail(id);
        model.addAttribute("member", member);
        return "mypage/info";
    }

    @PostMapping("/info")
    public String updateMember(@Valid MemberUpdateDto memberUpdateDto, Model model) {
        //System.out.println("memberUpdateDto : " + memberUpdateDto);
        customUserDetailsService.updateMember(memberUpdateDto);
        return "redirect:/mypage/info";
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

    @GetMapping("/changePw")
    public String changePwForm(Model model) {
        String id = memberService.getLoginId();
        Member member = memberService.memberDetail(id);
        model.addAttribute("member", member);
        return "mypage/changePw";
    }

    @PostMapping("/changePw")
    public String changePw(@Valid MemberPwDTO memberPwDTO, BindingResult bindingResult, Model model) {
        //String id = memberService.getLoginId();
        customUserDetailsService.updatePw(memberPwDTO, memberService.getLoginId());
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
        return "redirect:/mypage/info";
    }

}
