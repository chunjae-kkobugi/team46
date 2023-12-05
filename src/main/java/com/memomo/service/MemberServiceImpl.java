package com.memomo.service;

import com.memomo.entity.Member;
import com.memomo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MemberServiceImpl implements MemberService{
    @Autowired
    private MemberRepository memberRepo;
    @Autowired
    private BCryptPasswordEncoder pwEncoder;


    @Override
    public Member join(Member member) {
        // id, pw, email, tel 필수
        return memberRepo.save(member);
    }

    @Override
    public boolean idCheck(String id) {
        Optional<Member> member = memberRepo.findById(id);
        return member.isEmpty();
    }

    @Override
    public String login(String id, String pw) {
        Optional<Member> member = memberRepo.findById(id);

        if(member.isEmpty()){
            return "No such Id";
        }

        Member mem = member.orElseThrow();

        if(pwEncoder.matches(pw, mem.getPw())){
            // 입력한 비밀번호와 데이터베이스 비밀번호 일치 시 로그인 성공
            return "Login Success";
        }

        return "PW Not Match";
    }

    @Override
    public Member memberEdit(Member member) {
        return memberRepo.save(member);
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
