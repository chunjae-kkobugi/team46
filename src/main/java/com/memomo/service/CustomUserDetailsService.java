package com.memomo.service;

import com.memomo.dto.MemberPwDTO;
import com.memomo.dto.MemberUpdateDto;
import com.memomo.entity.Member;
import com.memomo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public String updateMember(MemberUpdateDto memberUpdateDto) {
        Optional<Member> optionalMember = memberRepository.findById(memberUpdateDto.getId());
        Member member = optionalMember.orElseThrow();
        member.updateName(memberUpdateDto.getName());
        member.updatePw(memberUpdateDto.getPw());
        member.updateTel(memberUpdateDto.getTel());
        member.updateEmail(memberUpdateDto.getEmail());
        member.updateSchool(memberUpdateDto.getSchool());
        member.updateAddr1(memberUpdateDto.getAddr1());
        member.updateAddr2(memberUpdateDto.getAddr2());
        member.updatePostcode(memberUpdateDto.getPostcode());

        // 회원 비밀번호 수정을 위한 패스워드 암호화
        //BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //String encodePw = encoder.encode(memberUpdateDto.getPw());
        //member.updatePw(encodePw);

        memberRepository.save(member);

        return member.getId();
    }

    public String updatePw(MemberPwDTO memberPwDTO) {
        //System.out.println("memberPwDTO : " + memberPwDTO);
        Optional<Member> optionalMember = memberRepository.findById(memberPwDTO.getId());
        Member member = optionalMember.orElseThrow();
        // 회원 비밀번호 수정을 위한 패스워드 암호화
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePw = encoder.encode(memberPwDTO.getNewPassword());
        member.updatePw(encodePw);
        memberRepository.save(member);
        return member.getId();
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        //System.out.println("유저디테일 id : " + id);
        Optional<Member> optionalMember = memberRepository.findById(id);
        //System.out.println("optionalMember : " + optionalMember);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            if (!member.getStatus().equals("ACTIVE")) {
                throw new IllegalStateException("User is not active");
            }
            return User.builder()
                    .username(member.getId())
                    .password(member.getPw())
                    .roles(member.getRole().toString())
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found with id: " + id);
        }


    }
}
