package com.make.bookclub.service;

import com.make.bookclub.dto.ItemFormDto;
import com.make.bookclub.entity.Item;
import com.make.bookclub.repository.ItemRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private ItemRepository itemRepository;

    // 저장소와 연결해서 상품 등록
    public Long saveItem(@Valid ItemFormDto itemFormDto){
        Item item = itemFormDto.createItem();
        itemRepository.save(item);
        return item.getId();
    }



}
