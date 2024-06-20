package com.make.bookclub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class MemberFormDto {
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    //비어있는 경우, 스페이스만 있는 경우도 안됨.
    private String name;

    @NotEmpty(message = "이메일은 필수 입력값입니다.")
    //비어있으면 안됨. 스페이스만 있는 경우 가능.
    private String email;

    private  String address;

    @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
    @Length(min = 4, max = 20)
    private  String password;

    private  String tel;
}
