package com.memomo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberFormDTO {
    private String id;
    private String pw;
    private String pw2;
    private String name;
    private String email;
    private String tel;
    private String addr1;
    private String addr2;
    private String postcode;
    private String school;
    private String status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
