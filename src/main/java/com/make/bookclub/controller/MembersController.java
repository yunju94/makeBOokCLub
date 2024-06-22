package com.make.bookclub.controller;

import com.make.bookclub.dto.MemberFormDto;
import com.make.bookclub.entity.Member;
import com.make.bookclub.service.Memberservice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor //초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성
public class MembersController {
    private final Memberservice memberservice;
    private  final PasswordEncoder passwordEncoder;

    @GetMapping("/new")
    public String memberform(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "/member/memberForm";
    }

    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
           //만약에 error가 있다면, 회원가입창으로 돌려보냄.
            return "member/memberForm";
        }
        //테이블에 넣어야함. 테이블에 넣는다. > entity_ createmembers
        try{
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            //entity와 repository에 넣어야함.
            // service >repository > savemember -> (유효성 검사)validate -> savemember -> resitory.save
            memberservice.saveMember(member);

        }catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/"; //error가 없다면 main으로 돌려보냄
    }

    @GetMapping("/login")
    public String memberlogin(){
        return "/member/memberLoginForm";
    }

    @GetMapping("/login/error")
    public String memberloginerror(Model model){
        model.addAttribute("loginErrorMsg", "아이디나 비밀번호를 확인해주세요.");
        return "/member/memberLoginForm";
    }

    @PostMapping("/login")
    public String memberloginPass(@Valid MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        //자료를 받음.
        // entity로 자료를 변경
        Member member =Member.createMember(memberFormDto, passwordEncoder);

        //entity를 repository로 가져가서 DB 저장
        memberservice.saveMember(member);

        return "redirect:/";
    }



}
