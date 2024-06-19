package com.make.bookclub.service;

import com.make.bookclub.dto.MemberFormDto;
import com.make.bookclub.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberserviceTest {
    @Autowired
    Memberservice memberservice;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("홍");
        memberFormDto.setTel("010");
        memberFormDto.setPassword("1234");
        return  Member.createMember((memberFormDto), passwordEncoder);

    }

    @Test
    @DisplayName("회원가입 테스트")
    public  void  saveMemverTest(){
        Member member = createMember();
        Member saveMember = memberservice.saveMember(member);

        assertEquals(member.getEmail(), saveMember.getEmail());
        assertEquals(member.getName(), saveMember.getName());
        assertEquals(member.getAddress(), saveMember.getAddress());
        assertEquals(member.getPassword(), saveMember.getPassword());
        assertEquals(member.getTel(), saveMember.getTel());
        assertEquals(member.getRole(), saveMember.getRole());

    }

    @Test
    @DisplayName("중복 가입 테스트")
    public  void  saveDuplicateMemberTest(){
        Member member1 = new Member();
        Member member2 = new Member();
        memberservice.saveMember(member1);

        Throwable e = assertThrows(IllegalStateException.class, ()->{
            memberservice.saveMember(member2);
        });
        assertEquals("이미 가입 된 회원입니다.", e.getMessage());
    }

}