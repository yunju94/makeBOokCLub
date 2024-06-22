package com.make.bookclub.repository;

import com.make.bookclub.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {//쿼리문 날리는 용
}
