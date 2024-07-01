package com.make.bookclub.repository;

import com.make.bookclub.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o where o.member.email = :email order by o.orderDate desc")
    List<Order> findOrders(@Param("email") String email, Pageable pageable);
    // 이메일 맞는 걸 내림차순으로 받는다.

    @Query("select count(o) from Order o where o.member.email = :email")
    Long countOrder(@Param("email") String email);
    // 이메일 넣으면 주문서 몇개인지 카운트
}
