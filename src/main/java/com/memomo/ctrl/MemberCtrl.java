package com.memomo.ctrl;

import com.memomo.dto.*;
import com.memomo.entity.Member;
import com.memomo.repository.MemberRepository;
import com.memomo.service.BoardService;
import com.memomo.service.CustomUserDetailsService;
import com.memomo.service.EmailService;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
    private final EmailService emailService;

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

    @GetMapping("/findPw")
    public String findPwForm() {
        return "member/findPw";
    }

    @PostMapping("/findPw")
    public String findPw(HttpServletRequest request, RedirectAttributes rttr, Model model) {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        //System.out.printf("id : %s, name : %s, email : %s\n", id, name, email);
        boolean found =  memberService.findId(email, name, id);
        //System.out.println("찾았는지 ? : " + found);

        rttr.addFlashAttribute("rt", true);
        rttr.addFlashAttribute("found", found);
        rttr.addFlashAttribute("id", id);
        rttr.addFlashAttribute("name", name);
        rttr.addFlashAttribute("email", email);
        if (found) {
            String tempPw = emailService.sendPw(email);
            Optional<Member> optionalMember = memberRepository.findById(id);
            Member member = optionalMember.orElseThrow();
            String encodedPw = passwordEncoder.encode(tempPw);
            member.updatePw(encodedPw);
            memberRepository.save(member);
        }
        return "redirect:/member/findPw";
    }

}
