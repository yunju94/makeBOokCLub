package com.make.bookclub.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
public class Cart {//장바구니
    //일대일 맵핑

    @Id // 키본키
    @Column(name = "cart_id") //칼럼명, 테이블 열 명 "cart_id"
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    @OneToOne(fetch = FetchType.LAZY)//일대일 맵핑
    @JoinColumn(name = "member_id") //member_id라고 되어있는 member의 칼럼과 조인할 것이다.
    private Member member; //스트링이 아닌 아예 객체 자체를 받아와야함.

    public static  Cart createCart(Member member){
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }


}
