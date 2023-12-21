package com.memomo;

import com.memomo.entity.Member;
import com.memomo.repository.MemberRepository;
import com.memomo.service.CustomUserDetailsService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@SpringBootTest
@Log4j2
public class MemberTest {

    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void changePw() {
        Optional<Member> optionalMember = memberRepository.findById("kim");
        Member member = optionalMember.orElseThrow();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePw = encoder.encode("4321");
        member.updatePw(encodePw);
        memberRepository.save(member);
    }
}
