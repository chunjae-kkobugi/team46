package com.memomo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
public class MemberFormDTO {
    @NotEmpty(message = "아이디는 필수 입력 값입니다.")
    @Pattern(regexp = "^[a-z0-9]{6,20}$", message = "영문 소문자+숫자조합 (6~20자 이내)")
    private String id;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8,20}$", message = "영문 대/소문자+숫자조합 (8~20자 이내)")
    private String pw;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8,20}$", message = "영문 대/소문자+숫자조합 (8~20자 이내)")
    private String pw2;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
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
