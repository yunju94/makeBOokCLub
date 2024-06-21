package com.make.bookclub.dto;

import com.make.bookclub.constant.ItemSellStatus;
import com.make.bookclub.entity.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemFormDto {

    private Long id; //상품 코드

    @NotBlank(message = "상품명은 필수 입니다.")
    private String itemNm; //상품명

    @NotBlank(message = "가격은 필수 입니다.")
    private int price; // 가격

    @NotBlank(message = "수량은 필수 입니다.")
    private int stockNumber; //수량

    @NotEmpty(message = "상세 설명은 필수 입니다.")
    private String itemDetail; // 상품 상세 설명

    private ItemSellStatus itemSellStatus;//상품 판매 상태

    private LocalDateTime regTime; //등록 시간

    private  LocalDateTime updateTime; // 수정 시간

    private  static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        //ItemFormDto - > Item연결
        return  modelMapper.map(this, Item.class);
    }

}
