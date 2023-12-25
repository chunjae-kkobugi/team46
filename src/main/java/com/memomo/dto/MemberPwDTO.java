package com.memomo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class MemberPwDTO {
    private String id;
    private String password;
    private String newPassword;
    private String newPasswordConfirm;
}
