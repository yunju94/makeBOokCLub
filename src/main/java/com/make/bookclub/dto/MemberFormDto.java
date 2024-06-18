package com.make.bookclub.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberFormDto {
    private String name;
    private String email;
    private  String nickname;
    private  String password;
    private  String tel;
}
