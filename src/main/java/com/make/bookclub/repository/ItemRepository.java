package com.make.bookclub.repository;

import com.make.bookclub.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>
                                , QuerydslPredicateExecutor<Item>
                                    , ItemRepositoryCustom{
    /*QuerydslPredicateExecutor
    Spring Data Jpa에 Querydsl을 사용하기 위해 제공되는 기능
    조인이 불가능
    단순조건에서만 사용가능
    엔티티 매니저 없이 사용 가능
    */
    List<Item> findByItemNm(String itemNm);
}
