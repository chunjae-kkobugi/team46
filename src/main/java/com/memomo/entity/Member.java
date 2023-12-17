package com.memomo.entity;

import com.memomo.dto.MemberFormDTO;
import com.memomo.dto.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.crypto.password.PasswordEncoder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Member extends BaseEntity {
    @Id
    @Column(length = 20)
    private String id;      // 로그인 아이디
    @Column(length = 300, nullable = false)
    private String pw;      // 로그인 비밀번호
    @Column(length = 100, nullable = false)
    private String name;    // 이름
    @Column(length = 100)
    private String email;   // 이메일
    @Column(length = 20)
    private String tel;     // 번화번호
    @Column(length = 100)
    private String addr1;   // 주소
    @Column(length = 200)
    private String addr2;   // 상세 주소
    @Column(length = 10)
    private String postcode;    // 우편번호
    @Column(length = 100)
    private String school;      // 재직 중인 학교
    @Column(length = 50)
    private String status;      // 회원 상태
    // 'REMOVE' 탈퇴
    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberFormDTO memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setId(memberFormDto.getId());
        String password = passwordEncoder.encode(memberFormDto.getPw());
        member.setPw(password);
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setTel(memberFormDto.getTel());
        member.setAddr1(memberFormDto.getAddr1());
        member.setAddr2(memberFormDto.getAddr2());
        member.setPostcode(memberFormDto.getPostcode());
        member.setSchool(memberFormDto.getSchool());
        member.setStatus("ACTIVE");
        member.setRole(Role.TEACHER);
        return member;
    }
}
