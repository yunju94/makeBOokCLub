package com.make.bookclub.entity;

import com.make.bookclub.constant.Role;
import com.make.bookclub.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;


@Entity
@Table(name= "member")
@Getter
@Setter
@ToString
public class Member extends BaseEntity{
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id; //회원번호
    private  String name; //이름
    @Column(unique = true)//중복 허용 안함
    private  String email;//이메일
    private  String password;//비밀번호
    private  String address;//주소
    @Column(unique = true)//중복 허용 안함
    private  String tel;//전화번호

    @Enumerated(EnumType.STRING)
    private Role role;//

    public static  Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setTel(memberFormDto.getTel());
        member.setRole(Role.ADMIN);
        return  member;
    }
}
