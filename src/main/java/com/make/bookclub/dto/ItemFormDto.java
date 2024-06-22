package com.make.bookclub.dto;

import com.make.bookclub.constant.ItemSellStatus;
import com.make.bookclub.entity.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {

    private Long id; //상품 코드 //item 과 연결되기 위해 똑같게 만들어야함.

    @NotBlank(message = "상품명은 필수 입니다.")
    private String itemNm; //상품명

    @NotNull(message = "가격은 필수 입니다.")
    private int price; // 가격

    @NotNull(message = "수량은 필수 입니다.")
    private int stockNumber; //수량

    @NotEmpty(message = "상세 설명은 필수 입니다.")
    private String itemDetail; // 상품 상세 설명

    private ItemSellStatus itemSellStatus;//상품 판매 상태


    //-----------------

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();//상품 이미지 정보

    private  List<Long> itemImgIds = new ArrayList<>();

    //-----------

    private  static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        //ItemFormDto - > Item연결
        return  modelMapper.map(this, Item.class);
    }

    public  static  ItemFormDto of(Item item){
        //item -> itemFormDto 연결
        return  modelMapper.map(item, ItemFormDto.class);
    }

}
