package com.memomo.dto;

import lombok.Data;

@Data
public class MemberUpdateDto {
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
}
