package com.memomo.service;

import com.memomo.entity.Member;
import com.memomo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        //System.out.println("유저디테일 id : " + id);
        Optional<Member> optionalMember = memberRepository.findById(id);
        //System.out.println("optionalMember : " + optionalMember);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
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
