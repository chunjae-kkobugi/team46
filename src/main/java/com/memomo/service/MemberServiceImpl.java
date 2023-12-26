package com.memomo.service;

import com.memomo.dto.MemberFormDTO;
import com.memomo.entity.Member;
import com.memomo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final ModelMapper modelMapper;

    private final MemberRepository memberRepo;

    //private final BCryptPasswordEncoder pwEncoder;


    @Override
    public void join(MemberFormDTO memberFormDTO) {
        // id, pw, email, tel 필수
        Member member = modelMapper.map(memberFormDTO, Member.class);
        //String ppw = pwEncoder.encode(member.getPw());
        //member.setPw(ppw);
        memberRepo.save(member);
    }

    @Override
    public boolean idCheck(String id) {
        Optional<Member> member = memberRepo.findById(id);
        return member.isEmpty();
    }

    @Override
    public String login(String id, String pw) {
        Optional<Member> member = memberRepo.findById(id);

        if (member.isEmpty()) {
            return "No such Id";
        }

        Member mem = member.orElseThrow();

//        if (pwEncoder.matches(pw, mem.getPw())) {
//            // 입력한 비밀번호와 데이터베이스 비밀번호 일치 시 로그인 성공
//            return "Login Success";
//        }

        return "PW Not Match";
    }

    @Override
    public void memberEdit(MemberFormDTO memberFormDTO) {
        Member member = modelMapper.map(memberFormDTO, Member.class);
        //String ppw = pwEncoder.encode(member.getPw());
        //member.setPw(ppw);
        System.out.println("멤버 : " + member);
        memberRepo.save(member);
    }

    @Override
    public String memberRemove(String id) {
        Member member = memberRepo.findById(id).orElseThrow();
        member.setStatus("REMOVE");

        // 제거된 아이디 반환
        return memberRepo.save(member).getId();
    }

    @Override
    public Member memberDetail(String id) {
        return memberRepo.findById(id).orElseThrow();
    }

    @Override
    public Member saveMember(Member member) {
        return memberRepo.save(member);
    }

    @Override
    public String getLoginId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        return "";
    }

    @Override
    public String findId(String email, String name) {
        Optional<Member> optionalMember = memberRepo.findByEmailAndName(email, name);
        if (optionalMember.isEmpty()) {
            return null;
        }
        return optionalMember.get().getId();
    }

    @Override
    public boolean existsId(String email, String id) {
        Optional<Member> optionalMember = memberRepo.findByEmailAndId(email, id);
        if (optionalMember.isEmpty()) {
            return false;
        }
        return true;
    }
}
