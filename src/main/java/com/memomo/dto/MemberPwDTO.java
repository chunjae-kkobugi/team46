package com.memomo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class MemberPwDTO {
    @NotEmpty(message = "현재 비밀번호는 필수 입력 값입니다.")
    /*@Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요.")*/
    private String password;

    @NotEmpty(message = "새 비밀번호는 필수 입력 값입니다.")
    /*@Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요.")*/
    private String newPassword;

    @NotEmpty(message = "새 비밀번호 확인은 필수 입력 값입니다.")
    /*@Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요.")*/
    private String newPasswordConfirm;
}
