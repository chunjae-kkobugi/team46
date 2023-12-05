package com.memomo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
}
