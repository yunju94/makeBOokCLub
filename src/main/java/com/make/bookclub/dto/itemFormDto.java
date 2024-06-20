package com.make.bookclub.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class itemFormDto {


    private Long id; //상품 코드

    @NotBlank(message = "상품명은 필수 입니다.")
    private String itemNm; //상품명

    @NotBlank(message = "가격은 필수 입니다.")
    private int price; // 가격

    @NotBlank(message = "수량은 필수 입니다.")
    private int stockNumber; //수량

    @NotEmpty(message = "상품명은 필수 입니다.")
    private String itemDetail; // 상품 상세 설명

}
