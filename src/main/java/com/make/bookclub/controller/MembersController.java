package com.make.bookclub.controller;

import com.make.bookclub.service.Memberservice;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor //초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성
public class MembersController {
    private final Memberservice memberservice;
    private  final PasswordEncoder passwordEncoder;

    @GetMapping("/new")
    public String memberform(){
        return "/member/memberForm";
    }
    @GetMapping("/login")
    public String memberlogin(){
        return "/member/memberLoginForm";
    }

}
