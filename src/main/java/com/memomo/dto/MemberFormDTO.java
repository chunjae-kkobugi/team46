package com.memomo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberFormDTO {
    private String id;
    private String pw;
    private String pw2;
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;
    private String email;
    @NotEmpty(message = "전화번호는 필수 입력 값입니다.")
    @Pattern(regexp = "[0-9]{3}-[0-9]{4}-[0-9]{4}", message = "010-XXXX-XXXX 형태로 입력해주세요.")
    private String tel;
    private String addr1;
    private String addr2;
    private String postcode;
    private String school;
    private String status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
