package com.memomo.service;

import com.memomo.dto.MemberDTO;
import com.memomo.entity.Member;
import com.memomo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final ModelMapper modelMapper;

    private final MemberRepository memberRepo;

    private final BCryptPasswordEncoder pwEncoder;


    @Override
    public void join(MemberDTO memberDTO) {
        // id, pw, email, tel 필수
        Member member = modelMapper.map(memberDTO, Member.class);
        String ppw = pwEncoder.encode(member.getPw());
        member.setPw(ppw);
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

        if (pwEncoder.matches(pw, mem.getPw())) {
            // 입력한 비밀번호와 데이터베이스 비밀번호 일치 시 로그인 성공
            return "Login Success";
        }

        return "PW Not Match";
    }

    @Override
    public void memberEdit(MemberDTO memberDTO) {
        Member member = modelMapper.map(memberDTO, Member.class);
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
}
