package com.make.bookclub.dto;

import com.make.bookclub.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
    private String itemNm;//상품명
    private  int count;//수량
    private  int orderPrice;//가격
    private  String imgUrl;//이미지 정보

    public  OrderItemDto(OrderItem orderItem, String imgUrl){
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }
}
