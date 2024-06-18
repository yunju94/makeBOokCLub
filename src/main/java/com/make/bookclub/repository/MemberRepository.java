package com.make.bookclub.repository;

import com.make.bookclub.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);
    Member findByTel(String tel);
}
